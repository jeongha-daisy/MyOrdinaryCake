<template>
  <main class="main-feed">
    <!-- 웹사이트 설명 이미지 영역 -->
    <div class="banner"><MainImage /></div>

    <div class="card-list">
      <div v-for="card in cards" :key="card.title" class="card">
        <div class="card-icon">{{ card.title.substring(0, 2) }}</div>
        <div>
          <h4>{{ card.title.substring(2) }}</h4>
          <p>{{ card.description }}</p>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import { defineComponent, ref } from "vue";
import MainImage from "./MainImage.vue";

export default defineComponent({
  name: "MainView",
  components: { MainImage },
  setup() {
    const cards = ref([
      {
        title: "🍰 어떤 케이크가 유행일까?",
        description:
          "취향별로 여러 케이크 가게를 탐색하고 각각의 가게가 제공하는 옵션과 설명을 쉽게 확인할 수 있습니다.",
      },
      {
        title: "😩 매번 메세지 보내기 귀찮아",
        description:
          "복잡했던 케이크 예약 절차를 온라인에서 간편하게 신청하고, 승인·입금·제작·수령까지 한눈에 확인할 수 있습니다.",
      },
      {
        title: "🧁 나만의 특별한 케이크를 만들어보자",
        description:
          "시트, 크림, 사이즈등 원하는 케이크 옵션을 직접 선택하고 디자인 요청 사항을 작성하여 나만의 케이크를 예약할 수 있습니다.",
      },
    ]);

    return {
      cards,
    };
  },
});
</script>

<style scoped>
.main-feed {
  flex: 1;
  padding: 40px;
  overflow-y: auto;
  background-color: #fff8e8;
  min-height: 100vh;
}

.banner {
  width: 100%;
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-bottom: 40px;
  border-radius: 8px;
}

.main-feed {
  flex: 1;
  padding: 40px;
  overflow-y: auto;
  background-color: #fff8e8;
  min-height: 100vh;
}

.banner {
  width: 100%;
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-bottom: 40px;
  border-radius: 8px;
}

.card-list {
  width: 100%;
  display: flex;
  flex-direction: row;
  gap: 30px;
  justify-content: center;
  flex-wrap: wrap;
}

.card {
  width: 200px;
  background-color: #fff;
  border-radius: 20px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
  padding: 30px 20px;
  text-align: center;
  transition: all 0.3s;
}

.card:hover {
  transform: translateY(-10px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
}

.card-icon {
  font-size: 40px;
  margin-bottom: 20px;
}

.card h4 {
  font-size: 20px;
  margin-bottom: 15px;
  color: #6f4e37;
}

.card p {
  font-size: 15px;
  color: #4b3c32;
  line-height: 1.6;
}
</style>
