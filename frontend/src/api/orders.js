import axios from "axios";

export const orderCake = async (orderData) => {
  const formData = new FormData();

  // 일반 필드들 추가
  formData.append("cake_design", orderData.cake_design);
  formData.append("cake_sheet", orderData.cake_sheet);
  formData.append("cake_cream", orderData.cake_cream);
  formData.append("cake_size", orderData.cake_size);
  formData.append("customer_id", orderData.customer_id);
  formData.append("customer_name", orderData.customer_name);
  formData.append("customer_number", orderData.customer_number);
  formData.append("deposit_name", orderData.deposit_name);
  formData.append("order_state", orderData.order_state);
  formData.append("order_date", orderData.order_date);
  formData.append("pick_up_date", orderData.pick_up_date);
  const pickupDateTime = `${orderData.pick_up_date} ${orderData.pickup_time}:00`;
  formData.append("pickup_time", pickupDateTime);
  formData.append("store_id", orderData.store_id);
  formData.append("cake_price", orderData.cake_price);
  formData.append("bank_account", orderData.bank_account);

  // 파일이 있는 경우만 추가
  if (orderData.cake_image) {
    formData.append("cake_image", orderData.cake_image);
  }
  console.log(formData, orderData);

  try {
    const res = await axios.post("/api/order/create", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    return res.data;
  } catch (error) {
    console.error("API error:", error);
    if (error.response && error.response.status === 413) {
      throw new Error("IMAGE_TOO_LARGE");
    }
    throw error;
  }
};
