<template>
  <div v-if="isLoading">
    <LoadingSpinner v-if="isLoading" />
  </div>
  <div v-else id="myGrid" class="ag-theme-mycustomtheme">
    <AgGridVue
      class="ag-theme-balham"
      style="height: 200px; width: 100%"
      :columnDefs="props.columnDefs"
      :rowData="rowData"
      :defaultColDef="defaultColDef"
      rowSelection="single"
      @grid-ready="onGridReady"
      @selectionChanged="onRowSelected"
      :key="gridKey"
    />

    <div class="cancel-section" style="margin-top: 16px">
      <InputBox
        placeholder="주문 번호"
        v-model="selectedOrderId"
        style="padding: 4px 8px; width: 200px; margin-right: 20px"
      />
      <div v-if="isLoading1">
        <LoadingSpinner v-if="isLoading" />
      </div>
      <SqureButton v-else label="취소 하기" :onClick="updateOrder" />
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import LoadingSpinner from "./LoadingSpinner.vue";
import InputBox from "./InputBox.vue";
import SqureButton from "./SqureButton.vue";
import { AgGridVue } from "ag-grid-vue3";

import "ag-grid-community/dist/styles/ag-grid.css";
import "ag-grid-community/dist/styles/ag-theme-balham.css";
import axios from "axios";
export default {
  components: { AgGridVue, LoadingSpinner, InputBox, SqureButton },
  props: {
    fetchFn: {
      type: Function,
      required: true,
    },
    columnDefs: {
      type: Array,
      required: true,
    },
  },
  setup(props) {
    const isLoading = ref(false);
    const isLoading1 = ref(false);
    const rowData = ref([]);
    const selectedOrderId = ref("");
    const defaultColDef = {
      resizable: true,
      sortable: true,
      filter: true,
    };

    onMounted(async () => {
      isLoading.value = true;
      try {
        const data = await props.fetchFn();

        if (Array.isArray(data)) {
          // fetchFn()로 받아온 데이터가 배열이라면
          rowData.value = data.map((item) => ({ ...item, cancel: false }));
        } else {
          rowData.value = [];
          console.warn("응답이 배열이 아님:", data);
        }

        isLoading.value = false;
      } catch (error) {
        console.error("CustomGrid fetch 실패:", error);
        isLoading.value = false;
      }
    });

    const gridKey = ref(0);
    const columnApi = ref(null);
    const onGridReady = (params) => {
      columnApi.value = params.columnApi;
      // 모든 열의 너비를 내용에 맞춰 자동 조정
      const allColumnIds = [];
      columnApi.value.getAllColumns().forEach((column) => {
        allColumnIds.push(column.getId());
      });
      columnApi.value.autoSizeColumns(allColumnIds, false);
    };

    const selected_state = ref("");
    const onRowSelected = (event) => {
      const rows = event.api.getSelectedRows();
      console.log(rows);
      selectedOrderId.value = rows[0].id;
      selected_state.value = rows[0].order_state;
    };

    const updateOrder = async () => {
      console.log(selectedOrderId.value);
      if (!selectedOrderId.value) {
        return;
      }
      //미승인, 승인 일 때는 취소 가능

      if (
        selected_state.value === "취소" ||
        selected_state.value === "거절" ||
        selected_state.value === "제작 중" ||
        selected_state.value === "픽업 완료" ||
        selected_state.value === "노쇼"
      ) {
        alert("취소할 수 없는 상태입니다.");
        selectedOrderId.value = "";
        return;
      }

      const payload = {
        order_id: selectedOrderId.value,
      };
      try {
        isLoading1.value = true;
        console.log(payload);
        const res = await axios.post("/api/order/update/user", payload);
        if (res.data === "UPDATEOK") {
          alert("취소하였습니다.");
        } else {
          alert("저장 실패");
          console.log(res.data);
        }
      } catch (e) {
        console.error(e);
        alert("서버 오류 발생");
      } finally {
        isLoading1.value = false;
      }
      await reloadData();
      selectedOrderId.value = "";
    };
    const reloadData = async () => {
      isLoading.value = true;
      try {
        const data = await props.fetchFn();
        if (Array.isArray(data)) {
          rowData.value = data.map((item) => ({ ...item, cancel: false }));
        } else {
          rowData.value = [];
          console.warn("응답이 배열이 아님:", data);
        }
      } catch (error) {
        console.error("CustomGrid fetch 실패:", error);
      }
      isLoading.value = false;
    };

    return {
      isLoading,
      isLoading1,
      gridKey,
      rowData,
      columnApi,
      onGridReady,
      defaultColDef,
      props,
      onRowSelected,
      selectedOrderId,
      updateOrder,
      reloadData,
      selected_state,
    };
  },
};
</script>

<style scoped>
.ag-theme-mycustomtheme {
  --ag-header-background-color: #f4ebe4;
  --ag-data-color: #6f4e37;
  --ag-header-foreground-color: #6f4e37;
  --ag-row-hover-color: rgba(192, 156, 131, 0.25);
  --ag-selected-row-background-color: rgba(192, 156, 131, 0.25);
}
.bottom-panel {
  margin-top: 20px;
  padding: 10px;
  background: #fff8e8;
  border: 1px solid #ccc;
  border-radius: 8px;
  text-align: center;
}
.info-box {
  width: 80%;
  padding: 8px;
  margin-bottom: 10px;
}
.guide-text {
  color: #6f4e37;
  margin-bottom: 10px;
}
.cancel-btn {
  padding: 8px 16px;
  background-color: #6f4e37;
  color: white;
  border: none;
  border-radius: 5px;
}
</style>
