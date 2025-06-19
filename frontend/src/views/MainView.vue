<template>
  <div class="app-container">
    <header class="app-header">
      <h1>My Ordinary Cake</h1>
      주문 제작 케이크 예약 시스템
    </header>
    <div class="layout">
      <LeftPanel />
      <RouterView />
      <RightPanel />
    </div>
  </div>
</template>

<script>
import { defineComponent } from "vue";
import LeftPanel from "../components/LeftPanel.vue";
import RightPanel from "../components/RightPanel.vue";

export default defineComponent({
  name: "App",
  components: {
    LeftPanel,
    RightPanel,
  },
  setup() {
    return {};
  },
});
</script>

<style>
@font-face {
  font-family: "mondeur";
  src: url("@/assets/fonts/Mondeur.ttf") format("truetype");
  font-weight: normal;
  font-style: normal;
}

.app-container {
  overflow: hidden;
}

.app-header {
  top: 0;
  padding: 80px;
  font-size: 15px;
  font-weight: bold;
  border-bottom: 1px solid #d9d9d9;
  text-align: center;
  color: #6f4e37;
  background-color: #fff8e8;
  font-family: "mondeur", sans-serif;
}

.layout {
  display: flex;
  width: 100vw;
}

html,
body {
  margin: 0;
  padding: 0;
  min-height: 100vh;
}
</style>
