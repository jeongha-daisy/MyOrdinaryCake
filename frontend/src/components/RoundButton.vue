<template>
  <button class="round-button" @click="props.onClick">
    {{ props.label }}
  </button>
</template>

<script>
export default {
  name: "RoundButton",
  props: {
    label: {
      type: String,
      default: "버튼",
    },
    onClick: {
      type: Function,
      default: () => {},
    },
  },
  setup(props) {
    return { props }; // props 자체를 반환하면 충돌 없음
  },
};
</script>

<style scoped>
.round-button {
  min-height: 40px;
  width: 100%;
  min-width: 160;
  padding: 0 20px;
  align-items: center;
  background-color: #6b4a2c;
  color: white;
  border: none;
  font-weight: 500;
  font-size: 14px;
  border-radius: 999px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  transition: transform 0.1s ease;
  cursor: pointer;
}

.round-button:hover {
  transform: translateY(-4px);
}
</style>
