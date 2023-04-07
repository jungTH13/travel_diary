<template>
  <div id="budget-container">
    <div id="budget-title">
      <div>
        <font-awesome-icon icon="fa-solid fa-tag" />
      </div>
      <input
        type="text"
        placeholder="내용을 입력하세요"
        v-model="budget.memo"
      />
    </div>

    <div id="plan-dates">
      <div id="date-title">
        <div>
          <font-awesome-icon icon="fa-regular fa-calendar-check" />
        </div>
        <h3>날짜 선택</h3>
      </div>
      <div id="select-date">
        <div>
          <input
            type="radio"
            id="pre"
            name="dates"
            value="여행 준비"
            v-model="budget.paymentDate"
          />
          <label for="pre">여행 준비</label>
        </div>
        <div v-for="(date, index) in planDate" :key="index">
          <input
            type="radio"
            :id="date"
            name="dates"
            :value="date"
            v-model="budget.paymentDate"
          />
          <label :for="date">{{ date }}</label>
        </div>
      </div>
    </div>

    <div id="payment-method">
      <div id="title">
        <div>
          <font-awesome-icon icon="fa-solid fa-wallet" style="color: #000000" />
        </div>
        <h3>결제 수단</h3>
      </div>
      <div id="select-date">
        <div>
          <input
            type="radio"
            id="card"
            name="payment"
            value="card"
            v-model="budget.paymentType"
          />
          <label for="card">카드</label>
        </div>
        <div>
          <input
            type="radio"
            id="cash"
            name="payment"
            value="cash"
            v-model="budget.paymentType"
          />
          <label for="cash">현금</label>
        </div>
      </div>
    </div>

    <div id="amount">
      <div>
        <font-awesome-icon icon="fa-solid fa-won-sign" />
        <h3>결제 금액</h3>
      </div>
      <input
        type="text"
        placeholder="금액 입력"
        v-model="budget.amountOfPayment"
      />
    </div>

    <div id="category">
      <div v-for="(cate, index) in categories" :key="index">
        <input
          type="radio"
          :id="cate"
          name="category"
          :value="cate"
          v-model="budget.planType"
        />
        <label :for="cate">{{ cate }}</label>
      </div>
    </div>

    <button class="submit-button" @click="handleBudget">저장</button>
  </div>
</template>

<style lang="scss">
#budget-container {
  margin-top: 40px;

  #budget-title {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    margin-top: 28px;
    input {
      margin-left: 10px;
      width: 180px;
      height: 28px;
      outline: none;
      border: none;
      border-bottom: 1px solid #ddd;
    }
  }

  #plan-dates,
  #payment-method {
    margin-top: 28px;
    > div {
      display: flex;
      flex-wrap: wrap;
      margin-top: 10px;

      h3 {
        font-weight: bold;
      }

      div {
        margin-right: 14px;
        input {
          display: none;
        }
        label {
          font-size: 0.9rem;
          color: #aaa;
          cursor: pointer;
        }
        input:checked ~ label {
          color: #74b83e;
          font-weight: bold;
        }
      }
    }
  }

  #amount {
    margin-top: 28px;
    > div {
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      margin-bottom: 10px;
    }
    h3 {
      font-weight: bold;
      margin-left: 14px;
    }
    input {
      width: 180px;
      height: 28px;
      text-align: right;
      outline: none;
      border: none;
      border-bottom: 1px solid #ddd;
    }
  }

  #category {
    margin-top: 28px;
    width: 100%;
    display: flex;
    flex-wrap: wrap;

    > div {
      margin-right: 14px;
      input {
        display: none;
      }
      label {
        font-size: 0.8rem;
        color: #aaa;
        cursor: pointer;
      }
      input:checked ~ label {
        color: #000;
        font-weight: bold;
      }
    }
  }

  button {
    margin-top: 40px;
  }
}
</style>

<script setup>
import { ref, reactive } from "vue";
import { useBudgetStore } from "../../../stores/budget";

const store = useBudgetStore();

const budget = reactive({
  travel: { id: 5 },
  memo: "",
  title: "",
  amountOfPayment: 0,
  paymentDate: "2023-10-02",
  paymentType: "card",
  planType: "",
  planTypeId: 0,
});

const categories = ref([
  "Food Expense",
  "Airfare",
  "Shopping",
  "Transportation",
  "Tourism",
  "Room Charge",
  "etc",
]);
const planDate = ref(["2023-10-02", "16", "17"]);

function handleBudget(e) {
  e.preventDefault();
  store.saveBudget(budget);
}
</script>
