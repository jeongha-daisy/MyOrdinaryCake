<template>
  <main class="detail-page">
    <div class="info-section" style="color: #6f4e37">
      <div class="shop-info">
        <div style="flex: 1">
          <img
            class="image"
            :src="storeData.store_img || defaultImg"
            alt="가게 이미지"
          />
          <p style="font-size: 18px">
            <strong>주문수: {{ store_order }}</strong>
          </p>
          <p><strong>가게 ID:</strong> {{ storeId }}</p>
          <p><strong>가게 이름:</strong> {{ storeData.store_name }}</p>
          <p><strong>주소:</strong> {{ storeData.store_address }}</p>
          <p><strong>번호:</strong> {{ storeData.store_number }}</p>
          <p class="desc">{{ storeData.store_desc }}</p>
        </div>
      </div>

      <div class="business-hours">
        <h2>영업 시간</h2>
        <CustomGrid
          :fetchFn="() => searchBusinessHour([storeId])"
          :columnDefs="columnDefs"
          :key="gridKeyBusinessHour"
        />
      </div>
    </div>

    <!-- 케이크 옵션 3개 -->
    <div class="grid-row">
      <div class="grid-box">
        <CustomGrid
          :fetchFn="() => searchCakeInfo([storeId, 'sheet'])"
          :columnDefs="sheetColumn"
        />
      </div>
      <div class="grid-box">
        <CustomGrid
          :fetchFn="() => searchCakeInfo([storeId, 'size'])"
          :columnDefs="sizeColumn"
        />
      </div>
      <div class="grid-box">
        <CustomGrid
          :fetchFn="() => searchCakeInfo([storeId, 'cream'])"
          :columnDefs="creamColumn"
        />
      </div>
    </div>
  </main>
</template>

<script>
import CustomGrid from "./CustomGrid.vue";
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import {
  get_store_info,
  searchCakeInfo,
  searchBusinessHour,
} from "../api/search.js";

export default {
  name: "DetailPage",
  components: { CustomGrid },
  setup() {
    const route = useRoute();
    const storeId = route.params.shopId;
    const store_order = ref(0);

    const defaultImg = new URL("@/assets/cake_image.jpeg", import.meta.url)
      .href;

    const columnDefs = [
      { field: "day_of_week", headerName: "요일", flex: 1, minWidth: 200 },
      { field: "open_time", headerName: "오픈", flex: 1, minWidth: 200 },
      { field: "close_time", headerName: "마감", flex: 1, minWidth: 200 },
    ];

    const sheetColumn = [
      { field: "sheet", headerName: "케이크 시트", flex: 1, minWidth: 200 },
    ];
    const sizeColumn = [
      { field: "size", headerName: "케이크 사이즈", flex: 1, minWidth: 200 },
    ];
    const creamColumn = [
      { field: "cream", headerName: "맛", flex: 1, minWidth: 200 },
    ];

    const storeData = ref({
      store_id: "",
      store_name: "",
      store_img: "",
      store_address: "",
      store_number: "",
      store_desc: "",
    });

    onMounted(async () => {
      const selectedStore = JSON.parse(sessionStorage.getItem("selectedStore"));
      console.log(selectedStore);

      try {
        const data = await get_store_info(storeId);
        console.log(data);
        if (data !== "NOT") {
          store_order.value = data.store_order;
          storeData.value = {
            store_id: storeId.value,
            store_name: selectedStore.store_name,
            store_img: "http://localhost:8000" + data.store_image,
            store_address: selectedStore.store_address,
            store_number: selectedStore.store_number,
            store_desc: selectedStore.store_desc,
          };
        }
      } catch (error) {
        console.error("Failed:", error);
      }
    });

    return {
      storeId,
      storeData,
      searchCakeInfo,
      searchBusinessHour,
      columnDefs,
      sheetColumn,
      sizeColumn,
      creamColumn,
      defaultImg,
      store_order,
    };
  },
};
</script>

<style scoped>
.detail-page {
  flex: 1;
  padding: 20px 40px;
  background-color: #fff8e8;
  min-height: 100vh;
}

.main-banner {
  position: relative;
  width: 100%;
  height: 300px;
  overflow: hidden;
  border-radius: 20px;
  margin-bottom: 40px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}

.main-banner img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.banner-overlay {
  position: absolute;
  bottom: 20px;
  left: 30px;
  background: rgba(0, 0, 0, 0.5);
  padding: 15px 25px;
  border-radius: 10px;
}

.banner-overlay h1 {
  color: white;
  font-size: 30px;
}

.info-section {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  margin-bottom: 10px;
}

.business-hours {
  background: rgba(255, 255, 255, 0.5);
  padding: 30px;
  border-radius: 15px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  flex: 1 1 45%;
  margin: 10px;
}

.shop-info {
  background: rgba(255, 255, 255, 0.5);
  padding: 30px;
  border-radius: 15px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  flex: 1;
  margin: 10px;
}

.shop-info h2,
.business-hours h2 {
  color: #6f4e37;
  margin-bottom: 20px;
}

.shop-info p {
  margin-bottom: 10px;
  font-size: 14px;
}

.shop-info .desc {
  color: #777;
  font-size: 14px;
}

table {
  width: 100%;
  border-collapse: collapse;
}
.image {
  width: 240px;
  height: 240px;
  object-fit: cover;
  border-radius: 10px;
  margin-bottom: 15px;
  box-shadow: 0 0px 10px rgba(0, 0, 0, 0.1);
}

td {
  padding: 10px;
  border-bottom: 1px solid #eee;
  font-size: 16px;
}

.options-section {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  gap: 20px;
}

.option-box {
  background: rgba(255, 255, 255, 0.5);
  padding: 30px;
  border-radius: 15px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  flex: 1 1 30%;
  text-align: center;
}

.option-box h3 {
  margin-bottom: 20px;
  color: #6f4e37;
}

.option-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
}

.option-grid span {
  background: rgba(255, 255, 255, 0.5);
  padding: 10px 20px;
  border-radius: 20px;
  font-size: 14px;
}

.grid-row {
  display: flex;
  gap: 20px;
}

.grid-box {
  flex: 1;
  background: rgba(255, 255, 255, 0.5);
  padding: 20px;
  border-radius: 16px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
}
</style>
