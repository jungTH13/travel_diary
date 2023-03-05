// https://nuxt.com/docs/api/configuration/nuxt-config
// export default defineNuxtConfig({

// })
export default {
  // modules: ["@nuxtjs/vuex"],
  buildModules: ["@nuxtjs/style-resources", "@nuxtjs/axios"],
  axios: {
    baseURL: "http://localhost:3000", // Used as fallback if no runtime config is provided
  },

  // Style resources module configuration
  // styleResources: {
  //   // your SCSS files location
  //   css: ["~/assets/css/app.scss"],
  // },
  css:
    // your SCSS files location
    ["~/assets/css/app.scss"],
};
