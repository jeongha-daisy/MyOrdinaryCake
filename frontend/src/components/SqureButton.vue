<template>
  <button class="add-button" @click="props.onClick">
    {{ props.label }}
  </button>
</template>

<script>
export default {
  name: "AddButton",
  props: {
    label: {
      type: String,
      default: "선택",
    },
    onClick: {
      type: Function,
      default: () => {},
    },
  },
  setup(props) {
    return { props };
  },
};
</script>

<style scoped>
.add-button {
  background-color: #6f4e37;
  color: white;
  font-size: 12px;
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
  cursor: pointer;
  transition: transform 0.1s ease;
}

.add-button:hover {
  transform: translateY(-2px);
}
</style>
