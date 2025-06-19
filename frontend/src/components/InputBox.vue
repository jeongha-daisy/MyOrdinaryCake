<template>
  <input
    class="InputBox"
    :type="type"
    :placeholder="placeholder"
    :value="modelValue"
    @input="$emit('update:modelValue', $event.target.value)"
  />
</template>

<script>
export default {
  name: "InputBox",
  props: {
    type: {
      type: String,
      default: "text",
    },
    placeholder: {
      type: String,
      default: "입력",
    },
    modelValue: {
      type: String,
      default: "",
    },
  },
};
</script>

<style scoped>
.InputBox {
  background-color: transparent;
  border: none;
  border-bottom: 2px solid #6f4e37;
  outline: none;
  height: 40px;
  font-size: 16px;
  color: #6f4e37;
  width: 100%;
}

.InputBox::placeholder {
  color: #6f4e37;
  opacity: 0.5;
}
</style>
