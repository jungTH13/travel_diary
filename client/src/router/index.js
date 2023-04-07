import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import LoginView from "../views/LoginView.vue";
import NewTravelView from "../views/travel/NewTravelView.vue";
import NewCountryView from "../views/travel/NewCountryView.vue";
import NewBudgetView from "../views/plan/budget/NewBudgetView.vue";
import BudgetHomeView from "../views/plan/budget/BudgetHomeView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/login",
      name: "login",
      component: LoginView,
    },
    {
      path: "/travel",
      name: "travel",
      children: [
        {
          path: "country",
          name: "new-country",
          component: NewCountryView,
        },
        {
          path: "new",
          name: "new-travel",
          component: NewTravelView,
        },
      ],
    },
    {
      path: "/plan",
      name: "plan",
      children: [
        {
          path: "budget",
          name: "budget",
          children: [
            {
              path: "",
              name: "plan-budget",
              component: BudgetHomeView,
            },
            {
              path: "new",
              name: "new-plan-budget",
              component: NewBudgetView,
            },
          ],
        },
      ],
    },
  ],
});

export default router;
