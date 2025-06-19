<template>
  <div class="preview-popup">
    <h2>🍰 주문 미리보기 🍰</h2>

    <div class="preview-card">
      <div class="order-row">
        <span class="label">디자인 요청</span>
        <span class="value">{{ orderData.cake_design }}</span>
      </div>

      <div class="order-row image-row">
        <span class="label">디자인 이미지</span>
        <div class="value image-value">
          <template v-if="orderData.cake_image">
            <img :src="orderData.cake_image" alt="디자인 이미지" />
          </template>
          <template v-else> 없음 </template>
        </div>
      </div>

      <div class="order-divider" />

      <div class="order-row">
        <span class="label">시트</span>
        <span class="value">{{ orderData.cake_sheet }}</span>
      </div>

      <div class="order-row">
        <span class="label">크림</span>
        <span class="value">{{ orderData.cake_cream }}</span>
      </div>

      <div class="order-row">
        <span class="label">사이즈</span>
        <span class="value">{{ orderData.cake_size }}</span>
      </div>

      <div class="order-divider" />

      <div class="order-row">
        <span class="label">고객명</span>
        <span class="value">{{ orderData.customer_name }}</span>
      </div>

      <div class="order-row">
        <span class="label">연락처</span>
        <span class="value">{{ orderData.customer_number }}</span>
      </div>

      <div class="order-row">
        <span class="label">입금자명</span>
        <span class="value">{{ orderData.deposit_name }}</span>
      </div>

      <div class="order-divider" />

      <div class="order-row">
        <span class="label">픽업일</span>
        <span class="value">{{ orderData.pick_up_date }}</span>
      </div>

      <div class="order-row">
        <span class="label">픽업일</span>
        <span class="value">{{ orderData.pickup_time }}</span>
      </div>

      <div class="order-row">
        <span class="label">주문일</span>
        <span class="value">{{ orderData.order_date }}</span>
      </div>
      <div class="order-divider" />

      <div class="order-row">
        <span class="label">최종 금액</span>
        <span class="value">{{ orderData.cake_price }}</span>
      </div>

      <div class="order-row">
        <span class="label">입금 계좌</span>
        <span class="value">{{ orderData.bank_account }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
export default {
  name: "OrderPreview",
  props: {
    order: {
      type: Object,
      required: true,
    },
  },
  setup() {
    const orderData = ref({});
    onMounted(() => {
      const saved = sessionStorage.getItem("orderPreview");
      if (saved) {
        orderData.value = JSON.parse(saved);
      }
    });
    return {
      orderData,
    };
  },
};
</script>

<style scoped>
.preview-popup {
  background-color: #fff8e8;
  padding: 40px 20px;
  color: #6f4e37;
  min-height: 100vh;
  box-sizing: border-box;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  font-weight: bold;
  font-size: 24px;
}

.preview-card {
  background: white;
  border-radius: 16px;
  padding: 30px 20px;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.15);
  max-width: 500px;
  margin: auto;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
}

.label {
  font-weight: 600;
  width: 40%;
  font-size: 16px;
}

.value {
  width: 55%;
  text-align: right;
  font-size: 16px;
}

.order-divider {
  border-bottom: 1px dashed #6f4e37;
  margin: 10px 0;
  opacity: 0.5;
}

.image-value {
  width: 55%;
  text-align: right;
}
.image-value img {
  margin-top: 10px;
  width: 100%;
  max-height: 250px;
  object-fit: cover;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}
</style>
