import { createRouter, createWebHistory } from "vue-router";
import MainView from "../views/MainView.vue";
import MainFeed from "../components/MainFeed.vue";
import MyOrderFeed from "../components/MyOrderFeed.vue";
import MyOrderFeed_store from "../components/MyOrderFeed_store.vue";
import MakeOrderFeed from "../components/MakeOrderFeed.vue";
import ManageFeed from "../components/ManageFeed.vue";
import AuthFeed from "../components/AuthFeed.vue";
import SignUpFeed from "../components/SignUpFeed.vue";
import AllStoreFeed from "../components/AllStoreFeed.vue";
import DetailFeed from "../components/DetailFeed.vue";
import HowToUseFeed from "../components/HowToUseFeed.vue";
import SearchPopUpView from "../views/SearchPopUpView.vue";
import PreviewPopUpView from "../views/PreviewPopUpView.vue";

const routes = [
  {
    path: "/",
    component: MainView,
    children: [
      { path: "", component: MainFeed },
      { path: "myorder", component: MyOrderFeed },
      { path: "myorder_store", component: MyOrderFeed_store },
      { path: "makeorder", component: MakeOrderFeed },
      { path: "manage", component: ManageFeed },
      { path: "auth", component: AuthFeed },
      { path: "auth/signup1", component: SignUpFeed },
      { path: "howtouse", component: HowToUseFeed },
      { path: "explore", component: AllStoreFeed },
      { path: "detail/:shopId", component: DetailFeed },
    ],
  },
  {
    path: "/popup/search-cake",
    name: "SearchPopUpView",
    component: SearchPopUpView,
  },
  {
    path: "/popup/preview-order",
    name: "PreviewPopUpView",
    component: PreviewPopUpView,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
