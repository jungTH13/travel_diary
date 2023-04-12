import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import LoginView from "../views/LoginView.vue";
import NewTravelView from "../views/travel/NewTravelView.vue";
import NewCountryView from "../views/travel/NewCountryView.vue";
import NewBudgetView from "../views/plan/budget/NewBudgetView.vue";
import BudgetHomeView from "../views/plan/budget/BudgetHomeView.vue";
import PlanHomeView from "../views/plan/PlanHomeView.vue";
import ScheduleListView from "../views/plan/schedule/ScheduleListView.vue";
import ChecklistView from "../views/plan/checklist/ChecklistView.vue";
import BookHomeView from "../views/plan/book/BookHomeView.vue";
import BookListView from "../views/plan/book/BookListView.vue";
import BookFlightView from "../views/plan/book/BookFlightView.vue";
import BookHotelView from "../views/plan/book/BookHotelView.vue";
import BookFoodView from "../views/plan/book/BookFoodView.vue";
import BookOtherView from "../views/plan/book/BookOtherView.vue";
import BookTransportView from "../views/plan/book/BookTransportView.vue";

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
      path: "/plan/:id",
      name: "plan",
      component: PlanHomeView,
      children: [
        {
          path: "",
          name: "schedule",
          component: ScheduleListView,
        },
        {
          path: "book",
          name: "book",
          component: BookHomeView,
          children: [
            {
              path: "",
              name: "book-all",
              component: BookListView,
            },
            {
              path: "flight",
              name: "book-flight",
              component: BookFlightView,
            },
            {
              path: "hotel",
              name: "book-hotel",
              component: BookHotelView,
            },
            {
              path: "food",
              name: "book-food",
              component: BookFoodView,
            },
            {
              path: "transport",
              name: "book-transport",
              component: BookTransportView,
            },
            {
              path: "other",
              name: "book-other",
              component: BookOtherView,
            },
          ],
        },
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
        {
          path: "checklist",
          name: "checklist",
          component: ChecklistView,
        },
      ],
    },
  ],
});

export default router;
