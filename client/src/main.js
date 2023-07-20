import { createApp } from "vue";
import { createPinia } from "pinia";

import App from "./App.vue";
import router from "./router";

import { library } from "@fortawesome/fontawesome-svg-core";
import { fas } from "@fortawesome/free-solid-svg-icons";
import { far } from "@fortawesome/free-regular-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import Antd from "ant-design-vue";
import "ant-design-vue/dist/antd.css";
import vuetify from './plugins/vuetify'
import { loadFonts } from './plugins/webfontloader'

import 'vuetify/dist/vuetify.min.css';

//vue3에서 달력 컴포넌트 사용
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'

loadFonts()
library.add(fas, far);

const app = createApp(App).component("font-awesome-icon", FontAwesomeIcon);

app.component('VueDatePicker', VueDatePicker);
app.use(createPinia());
app.use(router);
app.use(vuetify);
app.use(Antd);

app.mount("#app");
