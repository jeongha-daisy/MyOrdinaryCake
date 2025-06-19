<template>
  <div class="frosted-spinner">
    <div></div>
    <div></div>
    <div></div>
    <div></div>
  </div>
</template>

<script>
export default {
  name: "LoadingSpinner",
};
</script>

<style scoped>
.frosted-spinner {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
}
.frosted-spinner div {
  width: 12px;
  height: 12px;
  background-color: #e4d1ba;
  border-radius: 50%;
  animation: bounce 0.6s infinite alternate;
}
.frosted-spinner div:nth-child(2) {
  animation-delay: 0.1s;
  background-color: #c09c83;
}
.frosted-spinner div:nth-child(3) {
  animation-delay: 0.2s;
  background-color: #99745a;
}
.frosted-spinner div:nth-child(4) {
  animation-delay: 0.3s;
  background-color: #6f4e37;
}

@keyframes bounce {
  to {
    transform: translateY(-10px);
  }
}
</style>
