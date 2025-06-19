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

    <div
      class="cancel-section"
      style="margin-top: 16px; display: flex; align-items: center"
    >
      <InputBox
        placeholder="주문 번호"
        v-model="selectedOrderId"
        style="padding: 4px 8px; width: 200px; margin-right: 20px"
      />
      <div class="toggle-box">
        <button
          v-for="option in statusOptions"
          :key="option"
          :class="['toggle-button', { active: selectedStatus === option }]"
          @click="selectedStatus = option"
        >
          {{ option }}
        </button>
      </div>
      <div v-if="isLoading1">
        <LoadingSpinner v-if="isLoading" />
      </div>
      <SqureButton v-else label="상태 업데이트" :onClick="updateOrder" />
    </div>

    <div
      v-if="selectedStatus === '승인'"
      class="cancel-section"
      style="margin-top: 16px; display: flex; align-items: center"
    >
      <InputBox
        placeholder="가격"
        v-model="cake_price"
        style="padding: 4px 8px; width: 200px; margin-right: 20px"
      />
      <InputBox
        placeholder="계좌 정보"
        v-model="bank_account"
        style="padding: 4px 8px; width: 200px; margin-right: 20px"
      />
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
    const selectedOrderState = ref("");
    const cake_price = ref("");
    const bank_account = ref("");
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

    const onRowSelected = (event) => {
      const rows = event.api.getSelectedRows();
      console.log(rows);
      selectedOrderId.value = rows[0].id;
      selectedOrderState.value = rows[0].order_state;
    };

    const updateOrder = async () => {
      console.log(selectedOrderId.value);
      if (!selectedOrderId.value) {
        return;
      }

      if (
        selectedOrderState.value === "취소" ||
        selectedOrderState.value === "픽업 완료" ||
        selectedOrderState.value === "노쇼"
      ) {
        alert("변경할 수 없는 주문입니다.");
        selectedOrderId.value = "";
        selectedOrderState.value = "";
        return;
      }

      if (
        selectedOrderState.value === "미승인" &&
        (selectedStatus.value === "픽업 완료" ||
          selectedStatus.value === "노쇼")
      ) {
        console.log("");
        alert("미승인 상태는 승인 혹은 거절만 선택 가능합니다.");
        return;
      }

      if (
        selectedOrderState.value === "승인" &&
        (selectedStatus.value === "픽업 완료" ||
          selectedStatus.value === "승인" ||
          selectedStatus.value === "노쇼")
      ) {
        console.log("");
        alert("승인 상태는 제작 중 혹은 거절만 선택 가능합니다.");
        return;
      }
      if (selectedStatus.value === "승인") {
        if (!cake_price.value || !bank_account.value) {
          console.log("");
          alert("항목을 입력해주세요!");
          return;
        }
      }

      if (
        selectedOrderState.value === "거절" &&
        selectedStatus.value !== "승인"
      ) {
        console.log("");
        alert("거절 상태는 승인만 선택 가능합니다.");
        return;
      }

      if (
        selectedOrderState.value === "미승인" &&
        (selectedStatus.value === "픽업 완료" ||
          selectedStatus.value === "노쇼")
      ) {
        console.log("");
        alert("미승인 상태는 승인 혹은 거절만 선택 가능합니다.");
        return;
      }

      const payload = {
        order_id: selectedOrderId.value,
        order_state: selectedStatus.value,
        cake_price: cake_price.value,
        bank_account: bank_account.value,
      };
      try {
        isLoading1.value = true;
        console.log(payload);
        const res = await axios.post("/api/order/update/store", payload);
        if (res.data === "UPDATEOK") {
          alert("상태를 변경하였습니다.");
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
      cake_price.value = "";
      bank_account.value = "";
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

    const statusOptions = ["승인", "제작 중", "거절", "노쇼", "픽업 완료"];
    const selectedStatus = ref("승인");

    return {
      statusOptions,
      selectedStatus,
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
      selectedOrderState,
      bank_account,
      cake_price,
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
.toggle-box {
  display: flex;
  background: #fff8e8;
  border: 1px solid #d3c0b0;
  border-radius: 8px;
  padding: 4px;
  margin-right: 20px;
}

.toggle-button {
  padding: 6px 12px;
  border: none;
  background: none;
  cursor: pointer;
  border-radius: 6px;
  font-weight: bold;
  color: #6f4e37;
  transition: 0.2s;
}

.toggle-button.active {
  background-color: #6f4e37;
  color: white;
}
</style>
