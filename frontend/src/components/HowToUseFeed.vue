<template>
  <div class="usage-guide">
    <h1>사용 방법 안내</h1>
    <p class="sub-title">처음 오셨나요? 이렇게 이용하시면 됩니다!</p>

    <div class="guide-container">
      <!-- 회원 안내 -->
      <div class="guide-box user-guide">
        <h2>🧑‍💼 회원 이용 안내</h2>

        <div class="step-box">
          <h3>가입 및 탐색</h3>
          <ul>
            <li>회원가입 하기</li>
            <li>가게 탐색 및 검색</li>
          </ul>
        </div>

        <div class="step-box">
          <h3>주문 프로세스</h3>
          <ul>
            <li>
              주문을 하면 처음에는
              <strong><span style="color: #000000">미승인</span></strong>
              상태로 접수돼요.
            </li>
            <li>
              판매자가 주문서를 확인하고, 문제가 없으면
              <strong><span style="color: green">승인</span></strong
              >으로 상태를 변경해요. 가격과 입금 계좌 정보를 확인할 수 있습니다.
            </li>
            <li>
              만약 주문이 잘못 접수되었거나 변경하고 싶다면,
              <strong><span style="color: #000000">미승인</span></strong
              >상태에서는 언제든 주문을
              <strong><span style="color: gray">취소</span></strong
              >할 수 있어요.
            </li>

            <li>
              판매자가 주문을
              <strong><span style="color: green">승인</span></strong
              >하면, 안내받은 계좌로 입금해 주세요.
            </li>
            <li>
              입금이 확인되면 케이크 제작이 시작되고, 픽업 날짜에 맞춰 준비가
              완료됩니다.
            </li>
            <li>
              픽업 당일에는 예약한 시간에 방문해서 케이크를 수령하면 됩니다.
            </li>
            <li>
              만약 픽업 날짜에 방문하지 못하면
              <strong><span style="color: red">노쇼</span></strong
              >로 기록될 수 있으니 꼭 방문해 주세요!
            </li>
          </ul>
        </div>
      </div>

      <!-- 점주 안내 -->
      <div class="guide-box store-guide">
        <h2>🏪 점주 이용 안내</h2>

        <div class="step-box">
          <h3>가입 및 등록</h3>
          <ul>
            <li>점주 회원가입 하기</li>
            <li>가게 정보 등록 (운영 시간, 케이크 정보 등)</li>
          </ul>
        </div>

        <div class="step-box">
          <h3>주문 프로세스</h3>
          <ul>
            <li>
              회원이 주문하면
              <strong><span style="color: #000000">미승인</span></strong> 상태로
              주문이 등록됩니다.
            </li>
            <li>
              주문 내용을 확인하고, 문제가 없다면 가격과 입금 계좌 정보를 입력해
              <strong><span style="color: green">승인</span></strong
              >해 주세요. 주문자는 안내받은 계좌로 입금합니다.
            </li>
            <li>
              주문서가 부적절할 경우엔
              <strong><span style="color: gray">거절</span></strong> 상태로
              변경할 수 있습니다.
            </li>

            <li>
              입금이 확인되면 제작 중 상태로 변경하고, 픽업 날짜까지 케이크
              제작을 완료해 주세요..
            </li>
            <li>
              픽업 당일, 주문자가 오지 않으면
              <strong><span style="color: red">노쇼</span></strong> 상태로
              변경할 수 있습니다. (향후 회원 관리에 활용됩니다.)
            </li>
            <li>
              주문자가 정상적으로 픽업하면
              <strong><span style="color: gray">픽업 완료</span></strong> 상태로
              변경하며 주문이 마무리됩니다.
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "UsageGuidePage",
};
</script>

<style scoped>
.usage-guide {
  flex: 1;
  padding: 50px 30px;
  overflow-y: auto;
  background-color: #fff8e8;
  text-align: center;
}

.usage-guide h1 {
  font-size: 36px;
  color: #6f4e37;
  margin-bottom: 10px;
}

.sub-title {
  font-size: 18px;
  color: #4b3c32;
  margin-bottom: 50px;
}

.guide-container {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 40px;
}

.guide-box {
  flex: 1 1 450px;
  padding: 40px 30px;
  border-radius: 25px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  text-align: left;
}

.user-guide {
  background: #e8d8c7;
}

.store-guide {
  background: #e8d8c7;
}

.guide-box h2 {
  font-size: 30px;
  margin-bottom: 30px;
  color: #333;
  text-align: center;
}

.step-box {
  margin-bottom: 30px;
  padding: 20px;
  border-radius: 15px;
  background: rgba(255, 255, 255, 0.6);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
}

.step-box h3 {
  font-size: 22px;
  margin-bottom: 15px;
  color: #6f4e37;
}

.step-box ul {
  list-style: none;
  padding-left: 0;
}

.step-box li {
  font-size: 14px;
  margin-bottom: 15px;
  position: relative;
  padding-left: 25px;
  color: #6f4e37;
}

.step-box li::before {
  content: "✔";
  position: absolute;
  left: 0;
  color: #6f4e37;
}
</style>
