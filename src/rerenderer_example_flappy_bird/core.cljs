(ns rerenderer-example-flappy-bird.core
  (:require-macros [cljs.core.async.macros :refer [go-loop]])
  (:require [cljs.core.async :refer [<! timeout]]
            [rerenderer.primitives :as p]
            [rerenderer.core :as r]))

(enable-console-print!)

(defn bird
  [{:keys [x y]}]
  (p/image {:width 36
            :height 36
            :sx 4
            :sy 980
            :x x
            :y y
            :src "bird"}))

(defn background
  [{:keys [offset]} & childs]
  (p/rectangle {:width 1088
                :height 600}
    (p/rectangle {:width 1088
                  :height 600
                  :x (- 0 offset)}
      (for [x (range 0 1088 288)]
        (p/image {:width 288
                  :height 512
                  :sx 0
                  :sy 0
                  :x x
                  :y 0
                  :src "bird"})))
    childs))

(defn view
  [{:keys [bird-position]}]
  (background {:offset (mod bird-position 288)}
              (bird {:x 0 :y 0})))

(def state (atom {:bird-position 0}))

(r/init! view state
         {:canvas (.getElementById js/document "canvas")})

(go-loop []
  (swap! state update-in [:bird-position] + 5)
  (<! (timeout 50))
  (recur))
