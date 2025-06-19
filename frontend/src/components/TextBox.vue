<template>
  <textarea
    class="TextBox"
    :placeholder="placeholder"
    :value="modelValue"
    @input="$emit('update:modelValue', $event.target.value)"
    rows="5"
  />
</template>

<script>
export default {
  name: "TextBox",
  props: {
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
.TextBox {
  width: 100%;
  background: rgb(255, 252, 250);
  border: 1.5px solid rgba(192, 156, 131, 0.5);
  font-size: 16px;
  color: #6f4e37;
  outline: none;
  padding: 12px;
  box-sizing: border-box;
  resize: none;
  line-height: 2;
  border-radius: 10px;
}

.TextBox::placeholder {
  color: #6f4e37;
  opacity: 0.5;
}
</style>
