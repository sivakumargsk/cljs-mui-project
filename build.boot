(set-env!
 :source-paths    #{"src/cljs"}
 :resource-paths  #{"resources/public"}
 :dependencies '[[adzerk/boot-cljs          "2.0.0"   :scope "test"]
                 [adzerk/boot-cljs-repl     "0.3.3"   :scope "test"]
                 [adzerk/boot-reload        "0.5.1"   :scope "test"]
                 [pandeiro/boot-http        "0.7.6"   :scope "test"]
                 [com.cemerick/piggieback   "0.2.1"   :scope "test"]
                 [org.clojure/tools.nrepl   "0.2.12"  :scope "test"]
                 [weasel                    "0.7.0"   :scope "test"]
                 [crisptrutski/boot-cljs-test "0.3.0" :scope "test"]
                 [tolitius/boot-check         "0.1.4" :scope "test"]
                 [org.clojure/clojure         "1.8.0" :scope "test"]
                 [org.clojure/clojurescript "1.9.494" :scope "test"]

                 [reagent "0.6.1" :exclusions [cljsjs/react cljsjs/react-dom]]
                 [re-frame "0.9.2"]
                 [bidi "2.0.16"]
                 [kibu/pushy "0.3.7"]
                 [cljs-react-material-ui "0.2.39"]
                 [cljsjs/material-ui-chip-input "0.11.2-0"]
                 ])

(require
 '[adzerk.boot-cljs            :refer [cljs]]
 '[adzerk.boot-cljs-repl       :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload          :refer [reload]]
 '[pandeiro.boot-http          :refer [serve]]
 '[crisptrutski.boot-cljs-test :refer [test-cljs]]
 '[tolitius.boot-check :as check])

;; ------------------------------------
;; Static code analysis
;; ------------------------------------

(deftask lint []
  (set-env! :source-paths #{"src/cljs" "test/cljs"})
  (comp
   (check/with-yagni)
   (check/with-eastwood)
   (check/with-kibit)
   (check/with-bikeshed)))

;; ------------------------------------
;; Development Mode
;; ------------------------------------

(deftask build []
  (comp (speak)
        (cljs)))

(deftask run []
  (comp (serve :port 3008)
        (watch)
        (cljs-repl)
        (reload)
        (build)))

(deftask development []
  (task-options! cljs
                 {:optimizations :none
                  :source-map true
                  :compiler-options {:asset-path "js/app.out"}}
                 reload {:on-jsload 'cljs-mui-project.app/mount-root})
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp
   (development)
   (run)
   (target :dir #{"target/dev"})))

;; ------------------------------------
;; Production Mode
;; ------------------------------------

(deftask production []
  (task-options! cljs {:optimizations :advanced})
  identity)

(deftask prod
  "Simple alias to run application in production mode"
  []
  (comp
   (production)
   (cljs)
   (target :dir #{"target/prod"})))

;; ------------------------------------
;; Testing Mode
;; ------------------------------------

(deftask testing []
  (set-env! :source-paths #(conj % "test/cljs"))
  identity)

;;; This prevents a name collision WARNING between the test task and
;;; clojure.core/test, a function that nobody really uses or cares
;;; about.
(ns-unmap 'boot.user 'test)

(deftask test []
  (comp (testing)
        (test-cljs :js-env :phantom
                   :exit?  true)))

(deftask auto-test []
  (comp (testing)
        (watch)
        (test-cljs :js-env :phantom)))
