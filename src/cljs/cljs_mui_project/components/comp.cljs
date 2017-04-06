(ns cljs-mui-project.components.comp
  (:require [cljsjs.material-ui]
            [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [cljs-react-material-ui.reagent :as ui]
            [cljs-react-material-ui.icons :as ic]
            [reagent.core :as r]
            [cljs-react-material-ui.chip-input.core :refer [chip-input]]
            [cljs-react-material-ui.chip-input.reagent :refer [chip-input]]))

;; Example with various components
(defn app-bar [state]
  [ui/mui-theme-provider
   [:div
    [ui/app-bar
     {:title "Title"
      :style {:paddingLeft (if (:show-drawer @state)
                             270
                             30)}
      :icon-element-left (r/as-element
                          [ui/icon-button
                           {:on-click #(swap! state update-in [:show-drawer] not)}
                           (ic/navigation-menu)])}]
    [ui/drawer
     {:docked true
      :open (:show-drawer @state)}
     [:div "Admin"]
     [:div "Sample"]]]])

(defn home-page []
  (let [state (r/atom {:show-drawer false})]
    (fn []
      [:div [app-bar state]])))
