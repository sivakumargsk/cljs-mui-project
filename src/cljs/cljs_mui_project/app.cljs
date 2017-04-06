(ns cljs-mui-project.app
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [cljs-mui-project.events]
            [cljs-mui-project.subs]
            [cljs-mui-project.routes :as routes]
            [cljs-mui-project.views :as views]
            [cljs-mui-project.config :as config]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "container")))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
