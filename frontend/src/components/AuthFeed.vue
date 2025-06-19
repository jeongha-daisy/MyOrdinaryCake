<template>
  <div class="main-feed">
    <div v-if="isLoading">
      <LoadingSpinner v-if="isLoading" />
    </div>
    <div v-else class="form-container">
      <h3>로그인</h3>
      <div v-if="isSuccess === false" style="color: red; font-size: 12px">
        아이디와 비밀번호를 다시 확인해주세요!
      </div>
      <div v-else></div>
      <InputBox v-model="user_id" placeholder="아이디" />
      <InputBox v-model="user_pass" type="password" placeholder="비밀번호" />
      <div class="buttons">
        <RoundButton label="로그인" :onClick="logIn" />
        <RoundButton label="회원가입" :onClick="signUp" />
      </div>
    </div>
  </div>
</template>

<script>
import InputBox from "./InputBox.vue";
import RoundButton from "./RoundButton.vue";
import LoadingSpinner from "./LoadingSpinner.vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { ref, nextTick } from "vue";
import { loginUser } from "@/api/login.js";

export default {
  name: "AuthFeed",
  components: {
    InputBox,
    RoundButton,
    LoadingSpinner,
  },
  setup() {
    const isLoading = ref(false);
    const isSuccess = ref();
    const user_id = ref("");
    const user_pass = ref("");
    const store = useStore();

    const router = useRouter();
    const logIn = () => {
      console.log("로그인 버튼 클릭!");

      isLoading.value = true;
      // 실제 로그인 로직 구현
      const loginForm = {
        user_id: user_id.value,
        user_pass: user_pass.value,
      };

      loginUser(loginForm)
        .then((data) => {
          console.log(data);
          if (data !== "NOT") {
            store.commit("set_id", data.user_id);
            store.commit("set_name", data.user_name);
            store.commit("set_mode", data.user_mode);
            store.commit("set_number", data.user_number);

            if (data.user_mode === 1) {
              alert(data.user_name + "님 안녕하세요!");
            } else {
              store.commit("set_image", data.store_image);
              store.commit("set_address", data.store_address);
              store.commit("set_desc", data.store_desc);
              alert(data.user_name + "점주님 안녕하세요!");
            }

            nextTick(() => {
              router.push("/");
            });
          } else {
            isSuccess.value = false;
          }
        })
        .catch((error) => {
          console.error("Login failed:", error);
        })
        .finally(() => {
          isLoading.value = false;
        });
    };
    const signUp = () => {
      router.push(`/auth/signup1`);
    };
    return {
      logIn,
      signUp,
      user_id,
      user_pass,
      isSuccess,
      isLoading,
      store,
    };
  },
};
</script>

<style scoped>
.main-feed {
  display: flex;
  flex: 1;
  padding: 100px 40px;
  overflow-y: auto;
  background-color: #fff8e8;
  height: 100vh;
  justify-content: center;
  color: #6f4e37;
}

.form-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
  width: 300px;
  text-align: center;
}

.buttons {
  display: flex;
  gap: 16px;
  justify-content: space-between;
  width: 100%;
}
</style>
