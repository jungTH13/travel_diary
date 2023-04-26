import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import LoginView from "../views/LoginView.vue";
import TravelView from "../views/travel/TravelView.vue";
import NewTravelView from "../views/travel/NewTravelView.vue";
import NewCountryView from "../views/travel/NewCountryView.vue";
import NewBudgetView from "../views/plan/budget/NewBudgetView.vue";
import BudgetHomeView from "../views/plan/budget/BudgetHomeView.vue";
import PlanHomeView from "../views/plan/PlanHomeView.vue";
import ScheduleListView from "../views/plan/schedule/ScheduleListView.vue";
import ChecklistView from "../views/plan/checklist/ChecklistView.vue";
import BookHomeView from "../views/plan/book/BookHomeView.vue";
import BookDetailView from "../views/plan/book/BookDetailView.vue";
import MapGoogle from "../components/MapGoogle.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path:"/map",
      name:"map",
      component: MapGoogle
    },
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
      component: TravelView,
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
        
        {
          path: ":id/plan",
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
            },
            {
              path:"book/:planType/:planId",
              name:"book-detail",
              component: BookDetailView,
            },
            {
              path: "budget",
              name: "budget",
              component: BudgetHomeView,
            },
            {
              path: "budget/:budgetId",
              name: "budget-detail",
              component: NewBudgetView,
            },
            {
              path: "checklist",
              name: "checklist",
              component: ChecklistView,
            },
          ],
        },
      ],
    },
    
  ],
});

export default router;
