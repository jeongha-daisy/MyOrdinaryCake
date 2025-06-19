<template>
  <nav class="left-panel">
    <ul>
      <li
        :class="['menu-item', { active: currentPage === '' }]"
        @click="changePage('')"
      >
        <span>🏠</span> <span>메인</span>
      </li>

      <li
        :class="['menu-item', { active: currentPage === 'howtouse' }]"
        @click="changePage('howtouse')"
      >
        <span>❔</span> <span>사용방법</span>
      </li>
      <li
        :class="['menu-item', { active: currentPage === 'explore' }]"
        @click="changePage('explore')"
      >
        <span>🎂</span> <span>둘러보기</span>
      </li>

      <li
        v-if="user_mode !== null && user_mode == 1"
        :class="['menu-item', { active: currentPage === 'myorder' }]"
        @click="changePage('myorder')"
      >
        <span>📋</span> <span>나의 예약</span>
      </li>
      <li
        v-if="user_mode !== null && user_mode == 2"
        :class="['menu-item', { active: currentPage === 'myorder_store' }]"
        @click="changePage('myorder_store')"
      >
        <span>🏪</span> <span>우리 가게 예약</span>
      </li>
    </ul>
  </nav>
</template>

<script>
import { defineComponent, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useStore } from "vuex";

export default defineComponent({
  name: "SideMenu",
  setup() {
    const store = useStore();
    const user_mode = computed(() => store.state.store_user_mode);
    const router = useRouter();
    const route = useRoute();

    const currentPage = computed(() => route.path.replace("/", ""));

    const changePage = (viewName) => {
      router.push(`/${viewName}`);
    };

    return {
      router,
      changePage,
      store,
      user_mode,
      currentPage,
    };
  },
});
</script>

<style scoped>
.left-panel {
  min-width: 240px;
  border-right: 1px solid #d9d9d9;
  background: #fff8e8;
  padding: 30px 20px;
  display: flex;
  flex-direction: column;
  box-shadow: 4px 0 10px rgba(0, 0, 0, 0.05);
}

.logo-box {
  display: flex;
  align-items: center;
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 40px;
  color: #6f4e37;
}

.logo-text {
  margin-left: 10px;
}

.left-panel ul {
  list-style: none;
  padding: 0;
  margin: 0;
  width: 100%;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 14px 18px;
  margin-bottom: 16px;
  border-radius: 16px;
  font-size: 16px;
  font-weight: 500;
  color: #6f4e37;
  cursor: pointer;
  transition: all 0.2s ease;
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.menu-item:hover {
  transform: translateY(-2px);
  background-color: #f7f1e8;
}

.menu-item.active {
  background-color: #6f4e37;
  color: white;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}
</style>
