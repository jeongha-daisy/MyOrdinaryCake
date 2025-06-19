<template>
  <main class="main-feed">
    <h1>케이크 가게 둘러보기</h1>
    <p class="sub-title">자세한 정보가 필요하다면 카드를 눌러보세요!</p>
    <div class="filter-box">
      <inputBox
        v-model="searchText"
        class="filter-input"
        placeholder="가게 이름 또는 주소로 검색"
      />
      <button class="filter-button" @click="applyFilter">검색</button>
      <button class="filter-reset" @click="resetFilter">초기화</button>
    </div>

    <div v-if="isLoading">
      <LoadingSpinner v-if="isLoading" />
    </div>

    <div v-else class="card-list">
      <div
        v-for="card in filteredCards"
        :key="card.store_name"
        class="card"
        @click="goToDetail(card)"
      >
        <img
          class="avatar"
          :src="card.store_img || defaultImg"
          alt="가게 이미지"
        />
        <h3>{{ card.store_name }}</h3>
        <p>{{ card.store_address }}</p>
        <p>{{ card.store_number }}</p>
        <p class="store-desc">{{ card.store_desc }}</p>
      </div>
    </div>
  </main>
</template>

<script>
import { ref, onMounted, computed } from "vue";
import { exploreStore } from "../api/search.js";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import LoadingSpinner from "./LoadingSpinner.vue";
import InputBox from "./InputBox.vue";

export default {
  name: "MainView",
  components: { LoadingSpinner, InputBox },
  setup() {
    const isLoading = ref(true);
    const cards = ref([]);
    const defaultImg = new URL("@/assets/cake_image.jpeg", import.meta.url)
      .href;
    const searchText = ref("");
    const appliedFilter = ref("");
    const router = useRouter();

    onMounted(async () => {
      isLoading.value = true;
      try {
        const data = await exploreStore();
        if (data !== "NOT" && Array.isArray(data)) {
          cards.value = data.map((item) => ({
            store_name: item.store_name,
            store_img: "http://localhost:8000" + item.store_image,
            store_address: item.store_address,
            store_number: item.store_number,
            store_desc: item.store_desc,
            store_id: item.store_id,
          }));
        }
      } catch (error) {
        console.error("Failed:", error);
      } finally {
        isLoading.value = false;
      }
    });

    const applyFilter = () => {
      appliedFilter.value = searchText.value.trim();
    };

    const resetFilter = () => {
      appliedFilter.value = "";
      searchText.value = "";
    };

    const filteredCards = computed(() => {
      if (!appliedFilter.value) return cards.value;
      const keyword = appliedFilter.value.toLowerCase();
      return cards.value.filter(
        (card) =>
          card.store_name.toLowerCase().includes(keyword) ||
          card.store_address.toLowerCase().includes(keyword)
      );
    });

    const store = useStore();

    const goToDetail = (card) => {
      console.log("카드 클릭:", card);
      sessionStorage.setItem("selectedStore", JSON.stringify(card));
      router.push({ path: `/detail/${card.store_id}` });
    };

    return {
      router,
      store,
      cards,
      isLoading,
      defaultImg,
      goToDetail,
      searchText,
      applyFilter,
      resetFilter,
      filteredCards,
    };
  },
};
</script>

<style scoped>
.main-feed {
  flex: 1;
  padding: 40px;
  overflow-y: auto;
  background-color: #fff8e8;
  min-height: 100vh;
}

h1 {
  font-size: 36px;
  color: #6f4e37;
  margin-bottom: 10px;
}

.sub-title {
  font-size: 18px;
  color: #4b3c32;
  margin-bottom: 50px;
}

.loading {
  font-size: 18px;
  color: gray;
  text-align: center;
  padding: 100px;
}

.card-list {
  width: 100%;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 30px;
}

.card {
  background-color: #fff;
  padding: 20px;
  border-radius: 15px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);

  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #6f4e37;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.card h3 {
  font-size: 20px;
  margin: 10px 0 5px 0;
  font-weight: bold;
}

.card p {
  font-size: 14px;
  margin: 3px 0;
}

.store-desc {
  font-size: 12px;
  color: gray;
  margin-top: 10px;
  text-align: center;
}

.avatar {
  width: 240px;
  height: 160px;
  object-fit: cover;
  border-radius: 10px;
  margin-bottom: 15px;
}

.filter-box {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  margin-bottom: 30px;
}

.filter-input {
  width: 300px;
  font-size: 16px;
}

.filter-button,
.filter-reset {
  background-color: #6f4e37;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: 0.3s;
}

.filter-button:hover,
.filter-reset:hover {
  background-color: #543728;
}
</style>
