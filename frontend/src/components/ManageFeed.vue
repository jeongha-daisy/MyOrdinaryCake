<template>
  <div class="main-feed">
    <div style="color: #6f4e37; margin-bottom: 10px">
      <h3>가게 운영 시간</h3>
      <p>
        가게 운영 시간을 입력해 주세요! 설정한 시간 안에서 손님들이 픽업 시간을
        선택할 수 있어요.
      </p>
    </div>
    <div class="container">
      <!-- 왼쪽 -->
      <div class="left-panel">
        <div v-for="day in days" :key="day.value" class="day-row">
          <label class="day-label">
            <input type="checkbox" v-model="selectedDays" :value="day.value" />
            {{ day.label }}
          </label>
          <div
            class="time-inputs"
            :class="{ hidden: !selectedDays.includes(day.value) }"
          >
            <input type="time" v-model="businessHours[day.value].open_time" />
            ~
            <input type="time" v-model="businessHours[day.value].close_time" />
          </div>
        </div>

        <div v-if="isLoading">
          <LoadingSpinner v-if="isLoading" />
        </div>
        <SqureButton
          v-else
          label="운영 시간 업데이트하기"
          :onClick="saveBusinessHours"
        />
      </div>

      <!-- 오른쪽 -->
      <div class="right-panel">
        <CustomGrid
          :fetchFn="() => searchBusinessHour([user_id])"
          :columnDefs="columnDefs"
          :key="gridKeyBusinessHour"
        />
      </div>
    </div>
    <div style="color: #6f4e37; margin-bottom: 80px; margin-top: 50px">
      <h3>케이크 정보</h3>
      <p>
        가게에서 판매하는 케이크 정보를 적어주세요! 주문서에 쓰이고, 손님들에겐
        "판매 중"인 케이크만 보여요 😊
      </p>
      <p>
        더이상 사용하지 않는 항목은 상태 변경을 눌러 품절 상태로 바꿀 수 있어요.
      </p>
    </div>
    <div class="grid-row">
      <div class="grid-box">
        <CustomGrid
          :fetchFn="() => searchCakeInfo([user_id, 'sheet'])"
          :columnDefs="sheetColumn"
          :key="gridKeySheet"
        />
        <div v-if="isLoading1">
          <LoadingSpinner v-if="isLoading1" />
        </div>
        <div v-else class="grid-controls">
          <InputBox
            v-model="sheet_input"
            placeholder="입력"
            class="input-box"
          />
          <SqureButton label="추가" :onClick="() => addRow('sheet')" />
        </div>
      </div>

      <div class="grid-box">
        <CustomGrid
          :fetchFn="() => searchCakeInfo([user_id, 'size'])"
          :columnDefs="sizeColumn"
          :key="gridKeySize"
        />
        <div v-if="isLoading2">
          <LoadingSpinner v-if="isLoading2" />
        </div>
        <div v-else class="grid-controls">
          <InputBox v-model="size_input" placeholder="입력" class="input-box" />
          <SqureButton label="추가" :onClick="() => addRow('size')" />
        </div>
      </div>

      <div class="grid-box">
        <CustomGrid
          :fetchFn="() => searchCakeInfo([user_id, 'cream'])"
          :columnDefs="creamColumn"
          :key="gridKeyCream"
        />
        <div v-if="isLoading3">
          <LoadingSpinner v-if="isLoading3" />
        </div>
        <div v-else class="grid-controls">
          <InputBox
            v-model="cream_input"
            placeholder="입력"
            class="input-box"
          />
          <SqureButton label="추가" :onClick="() => addRow('cream')" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import CustomGrid from "./CustomGrid.vue";
import SqureButton from "./SqureButton.vue";
import LoadingSpinner from "./LoadingSpinner.vue";
import InputBox from "./InputBox.vue";
import { searchBusinessHour, searchCakeInfo } from "@/api/search.js";
import { useStore } from "vuex";
import { ref, computed } from "vue";
import axios from "axios";

export default {
  components: {
    CustomGrid,
    SqureButton,
    LoadingSpinner,
    InputBox,
  },
  name: "MainView",
  setup() {
    const isLoading = ref(false);
    const isLoading1 = ref(false);
    const isLoading2 = ref(false);
    const isLoading3 = ref(false);
    const gridKeyBusinessHour = ref(0);
    const gridKeySheet = ref(0);
    const gridKeySize = ref(0);
    const gridKeyCream = ref(0);

    const sheet_input = ref("");
    const size_input = ref("");
    const cream_input = ref("");

    const store = useStore();
    const user_id = computed(() => store.state.store_user_id);
    const columnDefs = [
      { field: "day_of_week", headerName: "요일" },
      { field: "open_time", headerName: "오픈" },
      { field: "close_time", headerName: "마감" },
    ];

    const sheetColumn = [
      { field: "sheet", headerName: "케이크 시트" },
      { field: "is_available", headerName: "상태" },
      {
        headerName: "상태 변경",
        field: "action",
        cellRenderer: (params) => {
          const button = document.createElement("button");
          button.innerText = "상태 변경";
          button.onclick = () => updateRow("sheet", params.data);
          return button;
        },
      },
    ];

    const sizeColumn = [
      { field: "size", headerName: "케이크 사이즈" },
      { field: "is_available", headerName: "상태" },
      {
        headerName: "상태 변경",
        field: "action",
        cellRenderer: (params) => {
          const button = document.createElement("button");
          button.innerText = "상태 변경";
          button.onclick = () => updateRow("size", params.data);
          return button;
        },
      },
    ];

    const creamColumn = [
      { field: "cream", headerName: "케이크 크림" },
      { field: "is_available", headerName: "상태" },
      {
        headerName: "상태 변경",
        field: "action",
        cellRenderer: (params) => {
          const button = document.createElement("button");
          button.innerText = "상태 변경";
          button.onclick = () => updateRow("cream", params.data);
          return button;
        },
      },
    ];

    const days = [
      { label: "일요일", value: 0 },
      { label: "월요일", value: 1 },
      { label: "화요일", value: 2 },
      { label: "수요일", value: 3 },
      { label: "목요일", value: 4 },
      { label: "금요일", value: 5 },
      { label: "토요일", value: 6 },
    ];

    const selectedDays = ref([]);
    const businessHours = ref({
      0: { open_time: "", close_time: "" },
      1: { open_time: "", close_time: "" },
      2: { open_time: "", close_time: "" },
      3: { open_time: "", close_time: "" },
      4: { open_time: "", close_time: "" },
      5: { open_time: "", close_time: "" },
      6: { open_time: "", close_time: "" },
    });
    const saveBusinessHours = async () => {
      isLoading.value = true;
      const payload = [];

      selectedDays.value.forEach((day) => {
        const open = businessHours.value[day].open_time;
        const close = businessHours.value[day].close_time;

        if (open && close) {
          payload.push({
            store_id: user_id.value,
            day_of_week: day,
            open_time: open,
            close_time: close,
          });
        }
      });

      try {
        const res = await axios.post(
          "/api/store/update/business-hour",
          payload
        );
        if (res.data === "OK") {
          alert("운영 시간이 저장되었습니다.");
          gridKeyBusinessHour.value += 1;
        } else {
          alert("저장 실패");
        }
      } catch (e) {
        console.error(e);
        alert("서버 오류 발생");
      }
      isLoading.value = false;
    };

    // 공통 핸들러
    const addRow = async (key) => {
      if (key == "sheet") {
        if (!sheet_input.value) {
          alert("값을 입력하세요.");
          return;
        }
        const payload = {
          store_id: user_id.value,
          sheet: sheet_input.value,
        };
        try {
          isLoading1.value = true;
          console.log(payload);
          const res = await axios.post("/api/store/insert/sheet", payload);
          if (res.data === "OK") {
            alert("새로운 시트를 추가했습니다.");
          } else {
            alert("저장 실패");
            console.log(res.data);
          }
        } catch (e) {
          console.error(e);
          alert("서버 오류 발생");
        }

        gridKeySheet.value += 1;
        sheet_input.value = "";
      } else if (key == "size") {
        if (!size_input.value) {
          alert("값을 입력하세요.");
          return;
        }
        const payload = {
          store_id: user_id.value,
          size: size_input.value,
        };
        try {
          isLoading2.value = true;
          const res = await axios.post("/api/store/insert/size", payload);
          if (res.data === "OK") {
            alert("새로운 사이즈를 추가했습니다.");
          } else {
            alert("저장 실패");
            console.log(res.data);
          }
        } catch (e) {
          console.error(e);
          alert("서버 오류 발생");
        }

        gridKeySize.value += 1;
        size_input.value = "";
      } else {
        if (!cream_input.value) {
          alert("값을 입력하세요.");
          return;
        }
        const payload = {
          store_id: user_id.value,
          cream: cream_input.value,
        };
        try {
          isLoading3.value = true;
          const res = await axios.post("/api/store/insert/cream", payload);
          if (res.data === "OK") {
            alert("새로운 크림을 추가했습니다.");
          } else {
            alert("저장 실패");
            console.log(res.data);
          }
        } catch (e) {
          console.error(e);
          alert("서버 오류 발생");
        }
        gridKeyCream.value += 1;
        cream_input.value = "";
      }

      isLoading1.value = false;
      isLoading2.value = false;
      isLoading3.value = false;
    };

    const updateRow = async (key, data) => {
      console.log("상태변경 요청", data, key);
      var changeValue;
      var payload;

      if (data.is_available == "판매 중") {
        changeValue = "품절"; //로 변경
      } else {
        changeValue = "판매 중"; //으로 변경
      }

      if (key == "sheet") {
        payload = {
          store_id: user_id.value,
          sheet: data.sheet,
          is_available: changeValue,
        };
      } else if (key == "size") {
        payload = {
          store_id: user_id.value,
          size: data.size,
          is_available: changeValue,
        };
      } else {
        payload = {
          store_id: user_id.value,
          cream: data.cream,
          is_available: changeValue,
        };
      }

      console.log(payload);

      try {
        const res = await axios.post("/api/store/update/" + key, payload);
        if (res.data === "UPDATEOK") {
          alert("업데이트를 완료하였습니다.");
        } else {
          alert("저장 실패");
          console.log(res.data);
        }
      } catch (e) {
        console.error(e);
        alert("서버 오류 발생");
      }
      if (key == "sheet") {
        gridKeySheet.value += 1;
      } else if (key == "size") {
        gridKeySize.value += 1;
      } else {
        gridKeyCream.value += 1;
      }
    };

    return {
      user_id,
      searchBusinessHour,
      columnDefs,
      gridKeyBusinessHour,
      gridKeyCream,
      gridKeySheet,
      gridKeySize,

      searchCakeInfo,
      sheetColumn,
      sizeColumn,
      creamColumn,

      sheet_input,
      size_input,
      cream_input,

      addRow,
      updateRow,

      days,
      selectedDays,
      businessHours,
      saveBusinessHours,
      isLoading,
      isLoading1,
      isLoading2,
      isLoading3,
    };
  },
};
</script>

<style scoped>
.main-feed {
  display: flex;
  flex-direction: column;
  padding: 40px 60px;
  background-color: #fff8e8;
  min-height: 100vh;
  gap: 50px;
  box-sizing: border-box;
}

h3 {
  margin: 0 0 10px 0;
  font-size: 22px;
  color: #6f4e37;
}

p {
  margin: 0;
  color: #6f4e37;
  font-size: 15px;
}

.container {
  display: flex;
  gap: 40px;
  align-items: flex-start;
}

.left-panel {
  flex: 1;
  background: rgba(255, 255, 255, 0.5);
  padding: 20px;
  border-radius: 16px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
}

.day-row {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.day-label {
  width: 100px;
  font-size: 16px;
  color: #6f4e37;
}

input[type="checkbox"] {
  accent-color: #6f4e37;
  margin-right: 8px;
  width: 18px;
  height: 18px;
}

.time-inputs {
  display: flex;
  gap: 10px;
  align-items: center;
  visibility: visible;
}

.time-inputs.hidden {
  visibility: hidden;
}

input[type="time"] {
  padding: 6px 10px;
  border: 1px solid #d0c3b6;
  border-radius: 8px;
  font-size: 14px;
  color: #6f4e37;
}

.right-panel {
  flex: 1.5;
  background: rgba(255, 255, 255, 0.5);
  padding: 20px;
  border-radius: 16px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
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

.grid-controls {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.input-box {
  flex: 1;
}
</style>
