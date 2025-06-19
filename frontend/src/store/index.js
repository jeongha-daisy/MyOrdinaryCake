import { createStore } from "vuex";

export default createStore({
  state: {
    store_user_id:
      sessionStorage.getItem("user_id") || "아직 로그인을 하지 않았어요!",
    store_user_name: sessionStorage.getItem("user_name") || "이름 🍰",
    store_user_mode: sessionStorage.getItem("user_mode") || 0,
    store_user_number: sessionStorage.getItem("user_number") || "",
    store_shop_image: sessionStorage.getItem("shop_image") || null,
    store_shop_address: sessionStorage.getItem("shop_address") || "",
    store_shop_desc: sessionStorage.getItem("shop_desc") || null,
    selected_shop_id: "",
  },
  getters: {},
  mutations: {
    set_selected_shop_id(state, id) {
      state.selected_shop_id = id;
    },
    set_id(state, id) {
      state.store_user_id = id;
      sessionStorage.setItem("user_id", id);
    },
    set_name(state, name) {
      state.store_user_name = name;
      sessionStorage.setItem("user_name", name);
    },
    set_mode(state, mode) {
      state.store_user_mode = mode;
      sessionStorage.setItem("user_mode", mode);
    },
    set_number(state, number) {
      state.store_user_number = number;
      sessionStorage.setItem("user_number", number);
    },
    set_image(state, image) {
      state.store_shop_image = image;
      sessionStorage.setItem("shop_image", image);
    },
    set_address(state, address) {
      state.store_shop_address = address;
      sessionStorage.setItem("shop_address", address);
    },
    set_desc(state, desc) {
      state.store_shop_desc = desc;
      sessionStorage.setItem("shop_desc", desc);
    },
  },
  actions: {},
  modules: {},
});
