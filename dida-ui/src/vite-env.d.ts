/// <reference types="vite/client" />
import { DefineComponent } from 'vue'
declare module '*.png'
declare module '*.jpg'
declare module '*.jpeg'
declare global {
    interface Window {
        $message: any,
        $dialog:any
    }
}
declare module '*.vue' {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any, @typescript-eslint/ban-types
    const component: DefineComponent<{}, {}, any>
    export default component
}