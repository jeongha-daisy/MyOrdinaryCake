<template>
  <main class="main-feed">
    <!-- 주문자 정보 -->
    <section class="order-section">
      <h3>
        주문자 정보
        <span class="info-note">
          <label for="sameAsMember" class="clickable-label"
            >회원 정보와 동일</label
          >
        </span>
        <input
          type="checkbox"
          id="sameAsMember"
          @change="checkedAsMember"
          style="display: none"
        />
      </h3>

      <div class="order-inputs">
        <div class="input-group">
          <InputBox v-model="user_name" placeholder="이름" />
        </div>
        <div class="input-group">
          <InputBox v-model="user_number" placeholder="번호" />
        </div>
        <div class="input-group">
          <InputBox v-model="user_name2" placeholder="입금자명" />
        </div>
      </div>
    </section>

    <!-- 케이크 정보 -->
    <section class="cake-section">
      <h3>케이크 정보</h3>
      <div class="cake-search-box" @click="searchCake">
        <span v-if="selectedStoreName == ''" class="cake-label">
          케이크 가게 찾기 🔍
        </span>
        <span v-else class="cake-label" style="color: #6f4e37">{{
          selectedStoreName
        }}</span>
        <div class="cake-line" />
      </div>

      <!-- 그리드 3개 -->
      <div class="grid-row">
        <div class="grid-box">
          <CustomGrid2
            ref="customGridRef_sheet"
            :columnDefs="sheetColumnDefs"
            :onSearch="searchCakeInfo_user"
            :gridId="'sheet'"
            :searchParams="searchParams_sheet"
            @selectRow="handleSelectedRow"
          />
        </div>
        <div class="grid-box">
          <CustomGrid2
            ref="customGridRef_size"
            :columnDefs="sizeColumnDefs"
            :onSearch="searchCakeInfo_user"
            :gridId="'size'"
            :searchParams="searchParams_size"
            @selectRow="handleSelectedRow"
          />
        </div>
        <div class="grid-box">
          <CustomGrid2
            ref="customGridRef_cream"
            :columnDefs="creamColumnDefs"
            :onSearch="searchCakeInfo_user"
            :gridId="'cream'"
            :searchParams="searchParams_cream"
            @selectRow="handleSelectedRow"
          />
        </div>
      </div>
      <div class="textbox">
        <TextBox
          v-model="cake_design"
          placeholder="원하시는 디자인을 적어주세요! 자세하게 적을 수록 케이크 제작에 도움이 돼요."
        />
        <!-- 파일 선택 버튼 -->
        <div class="img-button">
          <SqureButton
            label="참고용 이미지 업로드"
            :onClick="triggerFileInput"
          />
          <input
            type="file"
            @change="onFileChange"
            ref="fileInput"
            style="display: none"
          />
          <div class="img-text">
            <span>선택된 파일: </span
            ><span v-if="fileName"> {{ fileName }}</span>
          </div>
        </div>
      </div>
      <!-- 그리드 3개 -->
      <div class="grid-row">
        <div class="grid-box">
          <CustomGrid2
            ref="customGridRef_hour"
            :columnDefs="hourColumnDefs"
            :onSearch="searchBusinessHour"
            :gridId="'hour'"
            :searchParams="searchParams_hour"
            @selectRow="handleSelectedRow"
          />
        </div>
      </div>
      <div>
        <label for="date">픽업 날짜 선택: </label>
        <input
          id="date"
          type="date"
          v-model="selectedDate"
          :min="today"
          @change="checkDay"
        />
        <label for="pickupTime">픽업 시간 선택:</label>
        <input
          id="pickupTime"
          type="time"
          step="600"
          v-model="pickupTime"
          @change="checkTime"
        />
      </div>
    </section>
    <div class="buttons">
      <div v-if="isLoading">
        <LoadingSpinner v-if="isLoading" />
      </div>
      <RoundButton
        v-else
        style="width: 30%"
        label="주문하기"
        :onClick="makeOrder"
      />
      <RoundButton style="width: 30%" label="미리보기" :onClick="showPreview" />
    </div>
  </main>
</template>

<script>
import { ref, computed, nextTick } from "vue";
import InputBox from "./InputBox.vue";
import TextBox from "./TextBox.vue";
import CustomGrid2 from "./CustomGrid2.vue";
import RoundButton from "./RoundButton.vue";
import SqureButton from "./SqureButton.vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { searchCakeInfo_user, searchBusinessHour } from "@/api/search.js";
import { orderCake } from "@/api/orders.js";
import LoadingSpinner from "./LoadingSpinner.vue";

export default {
  components: {
    CustomGrid2,
    InputBox,
    TextBox,
    RoundButton,
    SqureButton,
    LoadingSpinner,
  },
  setup() {
    const store = useStore();

    const selectedStoreName = ref("");
    const selectedStoreId = ref("");

    const user_name = ref("");
    const user_number = ref("");
    const user_name2 = ref("");
    const cake_sheet = ref("");
    const cake_size = ref("");
    const cake_cream = ref("");
    const cake_design = ref("");
    const user_id = computed(() => store.state.store_user_id);

    const businessHourValue = ref({});

    const searchParams_sheet = ref([]);
    const searchParams_size = ref([]);
    const searchParams_cream = ref([]);
    const searchParams_hour = ref([]);

    const sheetColumnDefs = [{ field: "sheet", headerName: "케이크 시트" }];
    const sizeColumnDefs = [{ field: "size", headerName: "케이크 사이즈" }];
    const creamColumnDefs = [{ field: "cream", headerName: "크림" }];
    const hourColumnDefs = [
      { field: "day_of_week", headerName: "요일" },
      { field: "open_time", headerName: "오픈" },
      { field: "close_time", headerName: "마감" },
    ];

    const customGridRef_sheet = ref(null);
    const customGridRef_size = ref(null);
    const customGridRef_cream = ref(null);
    const customGridRef_hour = ref(null);

    const callHandleSearch = async () => {
      customGridRef_sheet.value.handleSearch();
      customGridRef_size.value.handleSearch();
      customGridRef_cream.value.handleSearch();
      businessHourValue.value = await customGridRef_hour.value.handleSearch();
    };

    window.onPopupClose = (selectedStore) => {
      console.log("팝업이 닫힘");
      console.log(selectedStore);
      selectedStoreName.value = selectedStore.user_name;
      selectedStoreId.value = selectedStore.user_id;

      searchParams_sheet.value.push(selectedStoreId.value);
      searchParams_sheet.value.push("sheet");

      searchParams_size.value.push(selectedStoreId.value);
      searchParams_size.value.push("size");

      searchParams_cream.value.push(selectedStoreId.value);
      searchParams_cream.value.push("cream");

      searchParams_hour.value.push(selectedStoreId.value);

      callHandleSearch();
    };
    const searchCake = () => {
      window.open(
        "/popup/search-cake",
        "케이크가게정보",
        "width=600,height=400"
      );
    };

    const checkedAsMember = () => {
      user_name.value = store.state.store_user_name;
      user_number.value = store.state.store_user_number;
      user_name2.value = store.state.store_user_name;
    };

    const selectedDate = ref("");
    const today = new Date().toISOString().split("T")[0];
    const checkDay = () => {
      const days = [
        { label: "일요일", value: 0 },
        { label: "월요일", value: 1 },
        { label: "화요일", value: 2 },
        { label: "수요일", value: 3 },
        { label: "목요일", value: 4 },
        { label: "금요일", value: 5 },
        { label: "토요일", value: 6 },
      ];

      // 선택된 날짜의 요일을 정수로 받아오기
      const day_int = new Date(selectedDate.value).getDay();
      // 정수를 요일로
      const day_str = days.find((day) => day.value === day_int).label;

      if (Array.isArray(businessHourValue.value)) {
        //해당 요일이 운영 시간에 포함되어 있으면 넘어간다.
        if (
          businessHourValue.value.some((item) => item.day_of_week === day_str)
        ) {
          console.log(day_str);
          //운영 요일에 포함이 되어 있지 않으면 selectedDate을 초기화한다.
        } else {
          alert("픽업 가능한 요일이 아닙니다.");
          selectedDate.value = "";
        }
      } else {
        alert("케이크 가게 먼저 선택해주세요!");
        selectedDate.value = "";
      }
    };

    const pickupTime = ref("");

    const checkTime = () => {
      const days = [
        { label: "일요일", value: 0 },
        { label: "월요일", value: 1 },
        { label: "화요일", value: 2 },
        { label: "수요일", value: 3 },
        { label: "목요일", value: 4 },
        { label: "금요일", value: 5 },
        { label: "토요일", value: 6 },
      ];
      if (selectedDate.value === "") {
        alert("날짜를 먼저 선택해주세요.");
        pickupTime.value = "";
        return;
      }

      // 선택한 날짜의 요일을 정수로 받아오기
      const day_int = new Date(selectedDate.value).getDay();
      // 정수를 문자로
      const day_str = days.find((day) => day.value === day_int).label;

      const start = businessHourValue.value
        .find((day) => day.day_of_week == day_str)
        .open_time.slice(0, 5)
        .replace(":", "");

      const end = businessHourValue.value
        .find((day) => day.day_of_week == day_str)
        .close_time.slice(0, 5)
        .replace(":", "");

      const pickupTime_short = pickupTime.value.replace(":", "");

      if (Array.isArray(businessHourValue.value)) {
        if (pickupTime_short >= start && pickupTime_short <= end) {
          console.log(day_str);
        } else {
          alert("픽업 가능한 시간 아닙니다.");
          pickupTime.value = "";
        }
      } else {
        alert("케이크 가게를 먼저 선택해주세요!");
        pickupTime.value = "";
      }
    };

    const fileInput = ref(null);
    const selectedFile = ref(null);
    const fileName = ref("");

    const triggerFileInput = () => {
      if (fileInput.value) {
        fileInput.value.click();
      }
    };

    const onFileChange = (e) => {
      const file = e.target.files[0];
      if (file) {
        selectedFile.value = file;
        fileName.value = file.name;
      } else {
        fileName.value = "";
      }
    };

    const handleSelectedRow = ({ id, row }) => {
      if (id === "sheet") {
        cake_sheet.value = row.sheet;
        console.log(cake_sheet);
      } else if (id === "size") {
        cake_size.value = row.size;
        console.log(cake_size);
      } else if (id === "cream") {
        cake_cream.value = row.cream;
        console.log(cake_cream);
      }
    };

    const showPreview = () => {
      if (selectedFile.value) {
        const reader = new FileReader();
        reader.onload = () => {
          orderData.value = {
            cake_design: cake_design.value,
            cake_image: reader.result,
            cake_sheet: cake_sheet.value,
            cake_cream: cake_cream.value,
            cake_size: cake_size.value,
            customer_id: user_id.value,
            customer_name: user_name.value,
            customer_number: user_number.value,
            deposit_name: user_name2.value,
            order_state: "미승인",
            order_date: today,
            pick_up_date: selectedDate.value,
            pickup_time: pickupTime.value,
            store_id: selectedStoreId.value,
            cake_price: "승인 전 상태입니다.",
            bank_account: "승인 전 상태입니다.",
          };

          sessionStorage.setItem(
            "orderPreview",
            JSON.stringify(orderData.value)
          );

          window.open(
            "/popup/preview-order",
            "주문 미리보기",
            "width=600,height=800"
          );
        };
        reader.readAsDataURL(selectedFile.value);
      } else {
        // 파일이 없는 경우
        orderData.value = {
          cake_design: cake_design.value,
          cake_image: null,
          cake_sheet: cake_sheet.value,
          cake_cream: cake_cream.value,
          cake_size: cake_size.value,
          customer_id: user_id.value,
          customer_name: user_name.value,
          customer_number: user_number.value,
          deposit_name: user_name2.value,
          order_state: "미승인",
          order_date: today,
          pick_up_date: selectedDate.value,
          pickup_time: pickupTime.value,
          store_id: selectedStoreId.value,
          cake_price: "승인 전",
          bank_account: "",
        };

        sessionStorage.setItem("orderPreview", JSON.stringify(orderData.value));

        window.open(
          "/popup/preview-order",
          "주문 미리보기",
          "width=600,height=800"
        );
      }
    };

    const isLoading = ref(false);
    const router = useRouter();

    const makeOrder = () => {
      console.log("주문 클릭!");

      isLoading.value = true;
      if (
        !cake_design.value ||
        !cake_sheet.value ||
        !cake_size.value ||
        !cake_cream.value ||
        !user_name.value ||
        !user_number.value ||
        !user_name2.value ||
        !selectedDate.value ||
        !pickupTime.value ||
        !selectedStoreId.value
      ) {
        alert("입력되지 않은 항목이 있습니다.");
        console.log(selectedDate);
        isLoading.value = false;
        return;
      }
      orderData.value = {
        cake_design: cake_design.value,
        cake_image: selectedFile.value,
        cake_sheet: cake_sheet.value,
        cake_cream: cake_cream.value,
        cake_size: cake_size.value,
        customer_id: user_id.value,
        customer_name: user_name.value,
        customer_number: user_number.value,
        deposit_name: user_name2.value,
        order_state: "미승인",
        order_date: today,
        pick_up_date: selectedDate.value,
        pickup_time: pickupTime.value,
        store_id: selectedStoreId.value,
        cake_price: "승인 전",
        bank_account: "",
      };

      orderCake(orderData.value)
        .then((data) => {
          console.log(data);

          if (data !== "NOT") {
            nextTick(() => {
              alert(
                "주문 완료! 가게가 승인 후 가격과 계좌번호를 추가할 거예요. '내 예약'에서 확인하고 빠른 입금 부탁드려요!"
              );
              router.push("/myorder");
            });
          } else {
            alert("주문 실패!");
          }
        })
        .catch((error) => {
          console.error("order failed:", error);
          if (error === "IMAGE_TOO_LARGE") {
            alert("이미지 크기가 허용 범위를 초과했습니다.");
          } else {
            alert("주문 실패!");
          }
        })
        .finally(() => {
          isLoading.value = false;
        });
    };

    const orderData = {
      cake_design: "",
      cake_image: null,
      cake_sheet: "",
      cake_cream: "",
      cake_size: "",
      customer_id: "",
      customer_name: "",
      customer_number: "",
      deposit_name: "",
      order_state: "",
      order_date: "",
      pick_up_date: "",
      pickup_time: "",
      store_id: "",
      cake_price: "승인 전",
      bank_account: "",
    };

    return {
      orderData,
      callHandleSearch,
      searchCake,

      selectedStoreName,
      selectedStoreId,

      businessHourValue,
      selectedDate,
      checkDay,
      today,
      checkTime,
      pickupTime,

      fileInput,
      fileName,
      triggerFileInput,
      onFileChange,

      store,

      customGridRef_sheet,
      customGridRef_size,
      customGridRef_cream,
      customGridRef_hour,

      searchParams_sheet,
      searchParams_size,
      searchParams_cream,
      searchParams_hour,

      sizeColumnDefs,
      sheetColumnDefs,
      creamColumnDefs,
      hourColumnDefs,

      searchCakeInfo_user,
      searchBusinessHour,

      handleSelectedRow,

      checkedAsMember,
      user_name2,
      user_number,
      user_name,
      cake_cream,
      cake_size,
      cake_sheet,
      cake_design,
      user_id,
      selectedFile,

      showPreview,
      makeOrder,
      isLoading,
      router,
    };
  },
};
</script>

<style scoped>
.main-feed {
  width: 100%;
  padding: 50px;
  background-color: #fff8e8;
  color: #6f4e37;
}
.order-section {
  margin-bottom: 50px;
}

.cake-search-box {
  width: fit-content;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-bottom: 50px;
}

.cake-label {
  color: #b19c8f;
  font-size: 16px;
  margin-bottom: 10px;
  margin-left: 5px;
  margin-right: 10px;
}

.cake-line {
  width: 100%;
  border-bottom: 2px solid #6f4e37;
}

.placeholder {
  color: #6f4e37;
  opacity: 0.5;
  font-size: 16px;
}

.info-note .clickable-label {
  font-size: 14px;
  font-weight: normal;
  margin-left: 8px;
  color: #999;
  border-bottom: 1px solid #999; /* 밑줄 스타일 */
  cursor: pointer; /* 마우스 포인터 변경 */
}
.order-inputs {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.textbox {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin: 50px 0;
}

.input-group {
  display: flex;
  flex-direction: column;
  flex: 1 1 200px;
}

.full-width {
  width: 100%;
  margin-bottom: 50px;
}

.grid-row {
  display: flex;
  gap: 20px;
}

.grid-box {
  flex: 1;
}

input[type="date"],
input[type="time"] {
  border: 1px solid #ccc;
  padding: 6px;
  border-radius: 4px;
  margin: 50px 20px;
  color: #6f4e37;
}
.img-button {
  display: flex;
  justify-content: center;
  gap: 10px;
}
.img-text {
  display: flex;
  align-items: center;
}

.buttons {
  display: flex;
  gap: 20px;
  width: 100%;
}
</style>
