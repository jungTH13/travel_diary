// https://nuxt.com/docs/api/configuration/nuxt-config
// export default defineNuxtConfig({

// })
export default {
  // modules: ["@nuxtjs/vuex"],
  buildModules: ["@nuxtjs/style-resources"],

  // Style resources module configuration
  // styleResources: {
  //   // your SCSS files location
  //   css: ["~/assets/css/app.scss"],
  // },
  css:
    // your SCSS files location
    ["~/assets/css/app.scss"],
};
