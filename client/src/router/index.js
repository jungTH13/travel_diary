import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import LoginView from "../views/LoginView.vue";
import CountryView from "../views/plan/CountryView.vue";
import NewCountryView from "../views/plan/NewCountryView.vue";

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
      path: "/plan",
      name: "plan",
      children: [
        {
          path: "country",
          name: "country",
          component: CountryView,
        },
        {
          path: "new",
          name: "newCountry",
          component: NewCountryView,
        },
      ],
    },
  ],
});

export default router;
