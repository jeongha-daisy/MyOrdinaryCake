<template>
  <main class="main-feed">
    <div style="color: #6f4e37; margin-bottom: 10px">
      <h3>우리 가게 전체 예약 보기</h3>
      <p>상태를 변경하여 주문을 관리하세요.</p>
    </div>
    <OrderGrid_store
      :fetchFn="() => searchOrder_store(store_id)"
      :columnDefs="columnDefs"
      @cancelOrder="handleCancelOrder"
    />
  </main>
</template>

<script>
import { ref, computed } from "vue";
import { useStore } from "vuex";
import OrderGrid_store from "./OrderGrid_store.vue";
import { searchOrder_store } from "@/api/search.js";

export default {
  components: { OrderGrid_store },
  name: "MainView",
  setup() {
    const store = useStore();
    const store_id = computed(() => store.state.store_user_id);
    const columnDefs = ref([
      {
        headerName: "상세",
        field: "action",
        cellRenderer: (params) => {
          const button = document.createElement("button");
          button.innerText = "상세";
          button.onclick = () => {
            showPreview(params.data.id);
          };
          button.style =
            "background-color: #6f4e37; color: white; border: none; border-radius: 4px;";
          return button;
        },
      },

      {
        headerName: "주문번호",
        field: "id",
        sortable: true,
        filter: true,
        flex: 1,
        minWidth: 25,
        cellStyle: {
          display: "flex",
          alignItems: "center",
        },
      },
      {
        headerName: "승인 상태",
        field: "order_state",
        sortable: true,
        filter: true,
        flex: 1,
        minWidth: 100,
        cellStyle: {
          display: "flex",
          alignItems: "center",
        },
        cellRenderer: (params) => {
          const value = params.value;
          let color = "#000000"; // 기본색

          if (value === "제작 중") {
            color = "#2986f6";
          } else if (value === "승인") {
            color = "green";
          } else if (value === "취소") {
            color = "gray";
          } else if (value === "노쇼") {
            color = "red";
          } else if (value === "거절") {
            color = "gray";
          } else if (value === "픽업 완료") {
            color = "gray";
          }

          return `<span style="color: ${color}; font-weight: bold;">${value}</span>`;
        },
      },
      {
        headerName: "주문자 아이디",
        field: "customer_id",
        sortable: true,
        filter: true,
        flex: 2,
        minWidth: 200,
        cellStyle: {
          display: "flex",
          alignItems: "center",
        },
      },
      {
        headerName: "픽업 날짜",
        field: "pick_up_date",
        sortable: true,
        filter: true,
        flex: 1,
        minWidth: 200,
        cellStyle: {
          display: "flex",
          alignItems: "center",
        },
      },
      {
        headerName: "픽업 시간",
        field: "pickup_time",
        sortable: true,
        filter: true,
        flex: 1,
        minWidth: 200,
        cellStyle: {
          display: "flex",
          alignItems: "center",
        },
      },
      {
        headerName: "주문 날짜",
        field: "order_date",
        sortable: true,
        filter: true,
        flex: 1,
        minWidth: 200,
        cellStyle: {
          display: "flex",
          alignItems: "center",
        },
      },
    ]);
    const showPreview = (index) => {
      const orderData = JSON.parse(sessionStorage.getItem("orderData"));
      const filteredData = orderData.filter((row) => row[0] === index);
      console.log(filteredData[0][2]);

      if (filteredData[0][2]) {
        const imageUrl = "http://localhost:8000" + filteredData[0][2];
        console.log(imageUrl);
        orderData.value = {
          cake_design: filteredData[0][1],
          cake_image: imageUrl,
          cake_sheet: filteredData[0][3],
          cake_cream: filteredData[0][4],
          cake_size: filteredData[0][5],
          customer_id: filteredData[0][6],
          customer_name: filteredData[0][7],
          customer_number: filteredData[0][8],
          deposit_name: filteredData[0][9],
          order_state: filteredData[0][10],
          order_date: filteredData[0][11],
          pick_up_date: filteredData[0][12],
          pickup_time: filteredData[0][13],
          store_id: filteredData[0][14],
          cake_price: filteredData[0][15],
          bank_account: filteredData[0][16],
        };

        sessionStorage.setItem("orderPreview", JSON.stringify(orderData.value));

        window.open(
          "/popup/preview-order",
          "주문 미리보기",
          "width=600,height=800"
        );
      } else {
        // 파일이 없는 경우
        orderData.value = {
          cake_design: filteredData[0][1],
          cake_image: null,
          cake_sheet: filteredData[0][3],
          cake_cream: filteredData[0][4],
          cake_size: filteredData[0][5],
          customer_id: filteredData[0][6],
          customer_name: filteredData[0][7],
          customer_number: filteredData[0][8],
          deposit_name: filteredData[0][9],
          order_state: filteredData[0][10],
          order_date: filteredData[0][11],
          pick_up_date: filteredData[0][12],
          pickup_time: filteredData[0][13],
          store_id: filteredData[0][14],
          cake_price: filteredData[0][15],
          bank_account: filteredData[0][16],
        };

        sessionStorage.setItem("orderPreview", JSON.stringify(orderData.value));

        window.open(
          "/popup/preview-order",
          "주문 미리보기",
          "width=600,height=800"
        );
      }
    };

    return {
      columnDefs,
      searchOrder_store,
      showPreview,
      store,
      store_id,
    };
  },
};
</script>

<style scoped>
.main-feed {
  flex: 1;
  padding: 40px;
  background-color: #fff8e8;
  overflow-y: auto;
  height: 100vh;
}

.banner {
  width: 100%;
  height: 300px;
  background-color: #d3d3d3;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-bottom: 40px;
  border-radius: 8px;
}
</style>
