<template>
  <div v-if="isLoading">
    <LoadingSpinner v-if="isLoading" />
  </div>
  <div v-else id="myGrid" class="ag-theme-mycustomtheme">
    <AgGridVue
      class="ag-theme-alpine"
      style="height: 250px; width: 100%"
      :columnDefs="props.columnDefs"
      :rowData="rowData"
      :defaultColDef="defaultColDef"
      rowSelection="single"
      @grid-ready="onGridReady"
    />
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import LoadingSpinner from "./LoadingSpinner.vue";
import { AgGridVue } from "ag-grid-vue3";

import "ag-grid-community/dist/styles/ag-grid.css";
import "ag-grid-community/dist/styles/ag-theme-alpine.css";
export default {
  components: { AgGridVue, LoadingSpinner },
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
    const rowData = ref([]);
    const columnApi = ref(null);
    const defaultColDef = {
      resizable: true,
      sortable: true,
      filter: true,
    };

    const onGridReady = (params) => {
      columnApi.value = params.columnApi;
      const allColumnIds = [];
      columnApi.value.getAllColumns().forEach((column) => {
        allColumnIds.push(column.getId());
      });
      columnApi.value.autoSizeColumns(allColumnIds, false);
    };

    onMounted(async () => {
      isLoading.value = true;
      try {
        const data = await props.fetchFn();

        if (Array.isArray(data)) {
          rowData.value = data;
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

    return {
      columnApi,
      onGridReady,
      defaultColDef,
      rowData,
      props,
      isLoading,
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
