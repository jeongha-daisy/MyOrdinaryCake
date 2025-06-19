<template>
  <div class="main-banner">
    <div class="banner-decor decor-top-left"></div>
    <div class="banner-decor decor-bottom-right"></div>

    <div class="banner-content">
      <h1>My Ordinary Cake</h1>
      <p>여러 가게를 한 곳에서 쉽게 찾아보고<br />주문부터 예약까지 한 번에!</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "MainBanner",
  setup() {
    return {};
  },
};
</script>

<style scoped>
@font-face {
  font-family: "mondeur";
  src: url("@/assets/fonts/Mondeur.ttf") format("truetype");
  font-weight: normal;
  font-style: normal;
}
.main-banner {
  width: 100%;
  height: 300px;
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(135deg, #fffaf3, #99745a);
  padding-left: 60px;
  border-radius: 30px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.banner-content {
  z-index: 2;
}

.banner-content h1 {
  font-size: 30px;
  font-weight: 700;
  color: #6f4e37;
  margin-bottom: 25px;
  font-family: "mondeur", sans-serif;
}

.banner-content p {
  font-size: 18px;
  color: #4b3c32;
  margin-bottom: 35px;
  line-height: 1.6;
}

.banner-content button:hover {
  background-color: #543728;
}

.banner-image img {
  width: 400px;
  border-radius: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  z-index: 2;
}

.banner-decor {
  position: absolute;
  border-radius: 50%;
  opacity: 0.2;
  z-index: 1;
}

.decor-top-left {
  width: 300px;
  height: 300px;
  background: #ffecd2;
  top: -100px;
  left: -100px;
}

.decor-bottom-right {
  width: 400px;
  height: 400px;
  background: #ffe2e2;
  bottom: -150px;
  right: -150px;
}
</style>
