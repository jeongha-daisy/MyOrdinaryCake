<template>
  <div v-if="isLoading">
    <LoadingSpinner v-if="isLoading" />
  </div>
  <div v-else id="myGrid" class="ag-theme-mycustomtheme">
    <AgGridVue
      class="ag-theme-alpine"
      style="height: 250px; width: 100%"
      :columnDefs="props.columnDefs"
      :rowData="filteredData"
      :defaultColDef="defaultColDef"
      rowSelection="single"
      @grid-ready="onGridReady"
      @selectionChanged="onRowSelected"
      key="gridKey"
    />
  </div>
</template>

<script>
import { ref } from "vue";
import LoadingSpinner from "./LoadingSpinner.vue";
import { AgGridVue } from "ag-grid-vue3";

import "ag-grid-community/dist/styles/ag-grid.css";
import "ag-grid-community/dist/styles/ag-theme-alpine.css";
export default {
  components: { AgGridVue, LoadingSpinner },
  props: {
    columnDefs: {
      type: Array,
      required: true,
    },
    onSearch: {
      type: Function,
    },
    searchParams: {
      type: Array, // 배열로 받음
      default: () => [],
    },
    gridId: { type: String, required: true },
  },
  emits: ["selectRow"],
  setup(props, { expose, emit }) {
    const isLoading = ref(false);
    const gridKey = ref(0);
    const filteredData = ref([]);
    const columnApi = ref(null);
    const defaultColDef = {
      resizable: true,
      sortable: true,
      filter: true,
      flex: 1,
      minWidth: 200,
    };

    const onGridReady = (params) => {
      columnApi.value = params.columnApi;
      const allColumnIds = [];
      columnApi.value.getAllColumns().forEach((column) => {
        allColumnIds.push(column.getId());
      });
      columnApi.value.autoSizeColumns(allColumnIds, false);
    };

    const handleSearch = async () => {
      console.log("검색시작", props.searchParams);
      isLoading.value = true;

      try {
        const data = await props.onSearch(props.searchParams);
        if (data !== "NOT") {
          if (Array.isArray(data)) {
            filteredData.value = [...data];
            console.log("검색 완료", filteredData.value);
            return data;
          }
        } else {
          filteredData.value = [];
          return [];
        }
      } catch (error) {
        console.error("Failed:", error);
        throw error;
      } finally {
        isLoading.value = false;
        gridKey.value += 1;
      }
    };

    const onRowSelected = (event) => {
      const rows = event.api.getSelectedRows();
      if (rows.length > 0) {
        emit("selectRow", { id: props.gridId, row: rows[0] });
      }
    };

    expose({
      handleSearch,
      filteredData,
    });

    return {
      columnApi,
      onGridReady,
      defaultColDef,
      filteredData,
      props,
      isLoading,
      handleSearch,
      gridKey,
      onRowSelected,
    };
  },
};
</script>

<style scoped>
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
