<template>
  <main class="popup-container">
    <div class="top-row">
      <InputBox
        v-model="searchStoreName"
        class="name-input"
        placeholder="가게명"
      />
      <SqureButton label="검색" :onClick="handleSearch" />
    </div>

    <!-- 하단: 검색 결과 그리드 -->
    <div v-if="isLoading">
      <LoadingSpinner />
    </div>
    <div v-else class="grid-section">
      <div id="myGrid" class="ag-theme-mycustomtheme">
        <AgGridVue
          class="ag-theme-alpine"
          style="height: 250px; width: 100%"
          :columnDefs="columnDefs"
          :rowData="filteredData"
          :rowSelection="rowSelection"
          @selectionChanged="onSelectionChanged"
          key="gridKey"
        />
      </div>
    </div>
  </main>
</template>

<script>
import InputBox from "../components/InputBox.vue";
import SqureButton from "../components/SqureButton.vue";
import LoadingSpinner from "../components/LoadingSpinner.vue";
import { useStore } from "vuex";
import { ref, computed } from "vue";
import { searchStore } from "../api/search.js";
import { AgGridVue } from "ag-grid-vue3";

import "ag-grid-community/dist/styles/ag-grid.css";
import "ag-grid-community/dist/styles/ag-theme-alpine.css";

export default {
  components: { AgGridVue, InputBox, SqureButton, LoadingSpinner },
  setup() {
    const store = useStore();
    const user_id = computed(() => store.state.store_user_id);

    const isLoading = ref(false);
    const gridKey = ref(0);

    const searchStoreName = ref("");
    const searchText = ref("");

    const columnDefs = [
      { field: "user_name", headerName: "가게명" },
      { field: "store_address", headerName: "주소" },
      { field: "user_id", headerName: "아이디" },
    ];
    const filteredData = ref([]);
    const rowSelection = ref("single"); // 단일 선택 모드

    const handleSearch = () => {
      console.log("검색시작");
      isLoading.value = true;
      searchText.value = searchStoreName.value;
      searchStore(searchText.value)
        .then((data) => {
          console.log(data);
          if (data !== "NOT") {
            if (Array.isArray(data)) {
              filteredData.value = [...data];
              console.log("검색 완료", filteredData.value);
            }
          } else {
            filteredData.value = [];
          }
        })
        .catch((error) => {
          console.error("Failed:", error);
        })
        .finally(() => {
          isLoading.value = false;
          gridKey.value += 1;
        });
    };

    // 행 선택 이벤트 핸들러
    const onSelectionChanged = (event) => {
      const selectedRows = event.api.getSelectedRows();
      if (selectedRows.length > 0) {
        const selectedStore = selectedRows[0];
        console.log("선택된 가게:", selectedStore);
        // 선택된 데이터 부모에게 전달하고 창 닫기
        if (window.opener && window.opener.onPopupClose) {
          window.opener.onPopupClose(selectedStore);
        }
        window.close();
      }
    };

    return {
      columnDefs,
      searchStoreName,
      handleSearch,
      filteredData,
      user_id,
      isLoading,
      gridKey,
      searchStore,
      rowSelection,
      store,
      onSelectionChanged,
    };
  },
};
</script>

<style scoped>
.popup-container {
  width: 100%;
  height: 100vh; /* 전체 높이 꽉 채우기 */
  display: flex;
  flex-direction: column;
  justify-content: center; /* 수직 중앙 */
  padding: 0 100px;
  background-color: #fff8e8;
  box-sizing: border-box;
}

.top-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 30px;
}

.name-input {
  flex: 1;
  margin-right: 20px;
}
#myGrid {
  border: 0.5px solid rgba(192, 156, 131, 0.5);
  border-radius: 10px;
  overflow: hidden;
}

.ag-theme-mycustomtheme {
  --ag-grid-size: 8px;
  --ag-border-color: rgba(192, 156, 131, 0.5);

  --ag-header-background-color: #e8d8c7;
  --ag-background-color: rgb(255, 252, 250);
  --ag-odd-row-background-color: #faf6f3;

  --ag-alpine-active-color: #c09c83;
  --ag-foreground-color: #6f4e37;
  --ag-secondary-foreground-color: #6f4e37;
  --ag-data-color: #6f4e37;
  --ag-header-foreground-color: #6f4e37;
  --ag-range-selection-highlight-color: #c09c83;
  --ag-range-selection-border-color: #d9d9d9;

  --ag-row-hover-color: rgba(192, 156, 131, 0.25);
  --ag-selected-row-background-color: rgba(192, 156, 131, 0.25);
}
</style>
