<template>
  <div class="right-panel">
    <!-- 프로필 -->
    <div class="profile">
      <img class="avatar" :src="imageUrl" alt="프로필 이미지" />
      <div class="nickname">{{ user_name }}</div>
      <div class="user-id">{{ user_id }}</div>
    </div>

    <!-- 버튼 -->
    <RoundButton
      v-if="user_mode !== null && user_mode == 2"
      label="내 가게 관리하기"
      :onClick="openListPage"
    />
    <RoundButton
      v-if="user_mode !== null && user_mode == 1"
      label="주문서 작성하기"
      :onClick="openOrderPage"
    />
    <RoundButton
      v-if="user_mode === 0"
      label="로그인 / 회원가입"
      :onClick="openAuthPage"
    />
    <div style="height: 30px"></div>
    <span v-if="user_mode !== 0" class="info-note">
      <label v-if="user_mode !== 0" for="logout" class="clickable-label"
        >로그아웃</label
      >
    </span>
    <input
      v-if="user_mode !== 0"
      type="checkbox"
      id="logout"
      @change="logout"
      style="display: none"
    />
  </div>
</template>

<script>
import { computed } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import RoundButton from "./RoundButton.vue";

export default {
  name: "SidePanel",
  components: {
    RoundButton,
  },
  setup() {
    const store = useStore();

    const user_mode = computed(() => store.state.store_user_mode);
    const user_name = computed(() => store.state.store_user_name);
    const user_id = computed(() => store.state.store_user_id);
    const store_image = computed(() => store.state.store_shop_image);

    const router = useRouter();

    const openListPage = () => {
      router.push(`/manage`);
    };

    const openOrderPage = () => {
      router.push(`/makeorder`);
    };

    const openAuthPage = () => {
      router.push(`/auth`);
    };
    const imageUrl = computed(() => {
      return store_image.value
        ? "http://localhost:8000" + store_image.value
        : new URL("@/assets/cake_image.jpeg", import.meta.url).href;
    });

    const logout = () => {
      try {
        alert("로그아웃하시겠습니까?");
        sessionStorage.removeItem("user_id");
        sessionStorage.removeItem("user_name");
        sessionStorage.removeItem("user_mode");
        sessionStorage.removeItem("user_number");
        sessionStorage.removeItem("shop_image");
        sessionStorage.removeItem("shop_address");
        sessionStorage.removeItem("shop_desc");
      } catch (error) {
        console.error(error);
      }
      window.location.href = "/";
    };
    return {
      imageUrl,
      router,
      openListPage,
      openOrderPage,
      openAuthPage,
      user_mode,
      user_name,
      user_id,
      store_image,
      logout,
    };
  },
};
</script>

<style scoped>
.right-panel {
  font-family: sans-serif;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px 50px;
  min-width: 200px;
  border-left: 1px solid #d9d9d9;
  background-color: #fff8e8;
}

.profile {
  margin: 40px 0;
  text-align: center;
}

.avatar {
  width: 80px;
  height: 80px;
  background-color: #fff;
  border-radius: 10px;
  padding: 10px;
  margin: 0 auto 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.nickname {
  font-size: 16px;
  font-weight: 600;
  margin: 0 auto 05px;
}

.user-id {
  color: #aaa;
  font-size: 12px;
}
.info-note .clickable-label {
  font-size: 14px;
  font-weight: normal;
  margin-left: 8px;
  color: #999;
  border-bottom: 1px solid #999; /* 밑줄 스타일 */
  cursor: pointer; /* 마우스 포인터 변경 */
}
</style>
