import { createApp } from 'vue'
import './style.css'
import router from './router'
import App from './App'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

createApp(App)
const pinia = createPinia()

pinia.use(piniaPluginPersistedstate)
const app = createApp(App)
app.use(router)
app.use(pinia)
app.mount('#app')