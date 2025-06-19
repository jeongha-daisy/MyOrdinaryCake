import axios from "axios";

export const searchBusinessHour = async (store_id) => {
  console.log(store_id[0]);
  const days = [
    { label: "일요일", value: 0 },
    { label: "월요일", value: 1 },
    { label: "화요일", value: 2 },
    { label: "수요일", value: 3 },
    { label: "목요일", value: 4 },
    { label: "금요일", value: 5 },
    { label: "토요일", value: 6 },
  ];
  try {
    const res = await axios.post("/api/search/businesshour", {
      store_id: store_id[0],
    });

    if (Array.isArray(res.data)) {
      const mappedData = res.data.map((item) => {
        const dayLabel =
          days.find((d) => d.value === item.day_of_week)?.label ||
          item.day_of_week;
        return {
          ...item,
          day_of_week: dayLabel,
        };
      });
      return mappedData;
    }

    return res.data;
  } catch (error) {
    console.error("error:", error);
    throw error;
  }
};

export const searchCakeInfo = async (params) => {
  console.log(params);
  const state = [
    { label: "판매 중", value: true },
    { label: "품절", value: false },
  ];
  try {
    const res = await axios.post("/api/search/" + params[1], {
      store_id: params[0],
    });
    if (Array.isArray(res.data)) {
      const mappedData = res.data.map((item) => {
        const stateLabel =
          state.find((d) => d.value === item.is_available)?.label ||
          item.is_available;
        return {
          ...item,
          is_available: stateLabel,
        };
      });
      return mappedData;
    }
    return res.data;
  } catch (error) {
    console.error("error:", error);
    throw error;
  }
};

export const searchCakeInfo_user = async (params) => {
  console.log(params);
  const state = [
    { label: "판매 중", value: true },
    { label: "품절", value: false },
  ];
  try {
    const res = await axios.post("/api/search/" + params[1], {
      store_id: params[0],
    });

    if (Array.isArray(res.data)) {
      const mappedData = res.data
        .map((item) => {
          const stateLabel =
            state.find((d) => d.value === item.is_available)?.label ||
            item.is_available;
          return {
            ...item,
            is_available: stateLabel,
            is_available_raw: item.is_available, // 원본 값도 남겨놓을 수 있음 (선택사항)
          };
        })
        .filter((item) => item.is_available_raw === true); // 판매 중인 것만 필터링

      return mappedData;
    }
    return res.data;
  } catch (error) {
    console.error("error:", error);
    throw error;
  }
};

export const searchStore = async (store_name) => {
  console.log(store_name);
  try {
    const res = await axios.post("/api/search/store", {
      store_name: store_name,
    });

    return res.data;
  } catch (error) {
    console.error("error:", error);
    throw error;
  }
};

export const get_store_info = async (store_id) => {
  try {
    const res = await axios.post("/api/search/get_store_info", {
      store_id: store_id,
    });

    return res.data;
  } catch (error) {
    console.error("error:", error);
    throw error;
  }
};

export const exploreStore = async () => {
  try {
    const res = await axios.post("/api/explore");

    return res.data;
  } catch (error) {
    console.error("error:", error);
    throw error;
  }
};

export const searchOrder_user = async (user_id) => {
  try {
    const res = await axios.post("/api/search/order/user", {
      user_id: user_id,
    });

    // 1. 받은 데이터 전체를 세션에 저장
    sessionStorage.setItem("orderData", JSON.stringify(res.data));

    // 2. 필요한 컬럼만 추출
    const filteredData = res.data.map((order) => ({
      id: order[0],
      order_state: order[10],
      store_id: order[14],
      pick_up_date: order[12],
      pickup_time: order[13],
      order_date: order[11],
      cake_price: order[15],
      store_name: order[17],
    }));
    console.log(res.data);
    console.log(filteredData);
    return filteredData;
  } catch (error) {
    console.error("error:", error);
    throw error;
  }
};

export const searchOrder_store = async (store_id) => {
  try {
    const res = await axios.post("/api/search/order/store", {
      store_id: store_id,
    });

    // 1. 받은 데이터 전체를 세션에 저장
    sessionStorage.setItem("orderData", JSON.stringify(res.data));

    // 2. 필요한 컬럼만 추출
    const filteredData = res.data.map((order) => ({
      id: order[0],
      order_state: order[10],
      customer_id: order[6],
      pick_up_date: order[12],
      pickup_time: order[13],
      order_date: order[11],
    }));
    console.log(res.data);
    console.log(filteredData);
    return filteredData;
  } catch (error) {
    console.error("error:", error);
    throw error;
  }
};
