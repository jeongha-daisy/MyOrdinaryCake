<template>
  <div class="main-feed">
    <div v-if="isLoading">
      <LoadingSpinner v-if="isLoading" />
    </div>
    <div v-else class="form-container">
      <h3>회원가입</h3>

      <div class="radio-group">
        <div class="radio-item">
          <input type="radio" :value="1" v-model="user_mode" />
          케이크를 주문할 회원이에요.
        </div>
        <div class="radio-item">
          <input type="radio" :value="2" v-model="user_mode" />
          가게를 등록하고 싶은 사장님이에요.
        </div>
      </div>

      <div v-if="user_mode === 2">
        <!-- 숨겨진 파일 input -->
        <input
          type="file"
          ref="fileInput"
          @change="onFileChange"
          style="display: none"
        />
        <div class="preview-container">
          <img
            v-if="previewUrl"
            :src="previewUrl"
            alt="미리보기 이미지"
            class="preview-image"
          />
          <div v-else class="placeholder-box"></div>
        </div>
        <!-- SqureButton으로 파일 선택 -->
        <SqureButton
          label="가게 홍보용 이미지 고르기"
          :onClick="triggerFileInput"
        />

        <div v-if="fileName" class="selected-file">
          선택된 파일: {{ fileName }}
        </div>
      </div>

      <!-- 바깥 form-container만 사용 -->
      <div class="form-container">
        <div class="input-label">로그인 정보</div>
        <InputBox v-model="user_id" placeholder="아이디" />
        <InputBox v-model="user_pass" type="password" placeholder="비밀번호" />

        <!-- 이용자 전용 -->
        <template v-if="user_mode === 1">
          <div class="input-label">이용자 정보</div>
          <InputBox v-model="user_name" placeholder="이름 (실명 권장)" />
          <InputBox v-model="user_number" placeholder="휴대폰 번호" />
        </template>

        <!-- 가게 전용 -->
        <template v-else>
          <div class="input-label">가게 정보</div>
          <InputBox v-model="user_name" placeholder="가게 이름" />
          <InputBox v-model="user_number" placeholder="가게 전화번호" />
          <InputBox v-model="store_address" placeholder="주소" />
          <TextBox
            style="margin: 20px 0px"
            v-model="store_desc"
            placeholder="이용자들을 위해 가게를 간단히 소개해주세요 :)"
          />
        </template>
      </div>
      <RoundButton
        style="margin: 20px 0px"
        label="회원가입"
        :onClick="signUp"
      />
    </div>
  </div>
</template>

<script>
import InputBox from "./InputBox.vue";
import RoundButton from "./RoundButton.vue";
import SqureButton from "./SqureButton.vue";
import LoadingSpinner from "./LoadingSpinner.vue";
import TextBox from "./TextBox.vue";
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";

export default {
  name: "AuthFeed",
  components: {
    InputBox,
    RoundButton,
    LoadingSpinner,
    SqureButton,
    TextBox,
  },
  setup() {
    const router = useRouter();

    const isLoading = ref(false);
    const user_mode = ref(1); // 기본값: 이용자
    const user_id = ref("");
    const user_pass = ref("");
    const user_name = ref("");
    const user_number = ref("");
    const store_address = ref("");
    const store_desc = ref("");

    const signUp = async () => {
      isLoading.value = true;
      if (user_mode.value == 1) {
        console.log("회원으로 가입 버튼");
        if (
          !user_id.value ||
          !user_pass.value ||
          !user_name.value ||
          !user_number.value
        ) {
          alert("입력되지 않은 항목이 있습니다.");
          isLoading.value = false;
          return;
        }

        const body = {
          user_id: user_id.value,
          user_pass: user_pass.value,
          user_name: user_name.value,
          user_mode: user_mode.value,
          user_number: user_number.value,
        };
        axios
          .post("/api/auth/signup/user", body)

          .then((res) => {
            console.log(res);
            if (res.data === "OK") {
              isLoading.value = false;
              alert("회원가입 완료");
              isLoading.value = false;
              router.push(`/auth`);
            } else if (res.data === "NOT") {
              alert("회원 가입 오류");
              isLoading.value = false;
            } else if (res.data === "ALREADY") {
              alert("이미 있는 아이디입니다.");
              isLoading.value = false;
            }
          })
          .catch((error) => {
            alert("회원가입 실패");
            console.error("Error during login:", error);

            isLoading.value = false;
          });

        isLoading.value = false;
      } else {
        console.log("점주로 가입 버튼");
        if (
          !user_id.value ||
          !user_pass.value ||
          !user_name.value ||
          !user_number.value ||
          !selectedFile.value ||
          !store_address.value
        ) {
          alert("입력되지 않은 항목이 있습니다.");
          isLoading.value = false;
          return;
        }

        const formData = new FormData();

        formData.append("user_id", user_id.value);
        formData.append("user_pass", user_pass.value);
        formData.append("user_name", user_name.value);
        formData.append("user_mode", user_mode.value);
        formData.append("user_number", user_number.value);
        formData.append("store_image", selectedFile.value);
        formData.append("store_address", store_address.value);
        formData.append("store_desc", store_desc.value);

        axios
          .post("/api/auth/signup/store", formData, {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          })

          .then((res) => {
            console.log(res);
            if (res.data === "OK") {
              isLoading.value = false;
              alert("회원가입 완료");
              // imageUrl.value = "http://localhost:8000" + res.data;
              isLoading.value = false;
              router.push(`/auth`);
            } else if (res.data === "NOT") {
              alert("회원 가입 오류");
              isLoading.value = false;
            } else if (res.data === "ALREADY") {
              alert("이미 있는 아이디입니다.");
              isLoading.value = false;
            }
          })

          // 잘 전송이 안돼면 error를 받는다.
          .catch((error) => {
            if (error.response.status === 413) {
              alert("이미지 크기가 허용 범위를 초과했습니다.");
            } else {
              alert("회원가입 실패");
            }
            console.error("Error during login:", error);

            isLoading.value = false;
          });
      }
    };

    const checked = ref(false);

    const handleChange = () => {
      checked.value = true;
    };

    const fileInput = ref(null);
    const selectedFile = ref(null);
    const fileName = ref("");
    const previewUrl = ref("");

    const isFileSelected = computed(() => !!selectedFile.value);
    const triggerFileInput = () => {
      fileInput.value?.click();
    };

    const onFileChange = (event) => {
      const file = event.target.files[0];
      if (file) {
        selectedFile.value = file;
        fileName.value = file.name;
        previewUrl.value = URL.createObjectURL(file);
      }
    };

    return {
      router,
      signUp,
      checked,
      handleChange,
      user_mode,
      user_id,
      user_pass,
      user_name,
      user_number,
      store_address,
      store_desc,
      isLoading,
      fileInput,
      selectedFile,
      fileName,
      previewUrl,
      onFileChange,
      isFileSelected,
      triggerFileInput,
    };
  },
};
</script>

<style scoped>
.main-feed {
  display: flex;
  flex: 1;
  padding: 40px 40px;
  overflow-y: auto;
  background-color: #fff8e8;
  min-height: 100vh;
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

.radio-group {
  display: flex;
  flex-direction: column;

  text-align: left;
  gap: 10px;
  font-size: 16px;
  color: #6f4e37;
}

input[type="radio"] {
  accent-color: #6f4e37;
  margin-right: 6px;
}

.selected-text {
  margin-top: 10px;
  font-size: 14px;
  color: #6f4e37;
}

.store-description {
  width: 100%;
  min-height: 100px;
  background-color: #f4ebe4;
  border: none;
  border-radius: 8px;
  padding: 12px;
  font-size: 14px;
  resize: vertical; /* 사용자가 늘릴 수 있도록 */
  box-sizing: border-box;
  color: #333;
}
.selected-file {
  margin-top: 8px;
  font-size: 14px;
  color: #6f4e37;
}
.store-description::placeholder {
  color: #999;
}

.char-count {
  text-align: right;
  font-size: 12px;
  color: #6f4e37;
  margin-top: 4px;
}

.input-label {
  font-size: 14px;
  color: #6f4e37;
  text-align: left;
  font-weight: 500;
  margin: 40px 0px 0px 0px;
}
.preview-container {
  margin-top: 12px;
  width: 200px;
  height: 200px;
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.selected-text {
  margin-top: 10px;
  font-size: 14px;
  color: #6f4e37;
}

.selected-file {
  margin-top: 8px;
  font-size: 14px;
  color: #6f4e37;
}
.preview-container {
  margin: 12px auto;
  width: 150px;
  height: 150px;
  border-radius: 50%;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1.5px solid rgba(192, 156, 131, 0.5);
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.placeholder-box {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: rgb(255, 252, 250);
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
