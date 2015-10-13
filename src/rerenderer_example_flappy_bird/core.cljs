(ns rerenderer-example-flappy-bird.core
  (:require-macros [cljs.core.async.macros :refer [go-loop]])
  (:require [cljs.core.async :refer [<! timeout]]
            [cljs.core.match :refer-macros [match]]
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
  (p/rectangle {:width 800
                :height 512}
    (p/rectangle {:width 1088
                  :height 512
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

(defn get-barier-sprite-x
  [direction color]
  (match [direction color]
    [:up :red] 56
    [:down :red] 0
    [:up :green] 168
    [:down :green] 112))

(defn get-barier-sprite-y
  [direction height]
  (if (= direction :down)
    (+ 645 (- 320 height))
    645))

(defn get-barier-y
  [direction height]
  (if (= direction :up)
    (- 512 height)
    0))

(defn barier
  [{:keys [x height direction color]}]
  (let [sprite-x (get-barier-sprite-x direction color)
        sprite-y (get-barier-sprite-y direction height)
        y (get-barier-y direction height)]
    (p/image {:x x
              :y y
              :sx sprite-x
              :sy sprite-y
              :width 52
              :height height
              :src "bird"})))

(defn view
  [{:keys [bird-position bariers bird-y]}]
  (background {:offset (mod bird-position 288)}
              (bird {:x 50 :y bird-y})
              (for [{:keys [position height direction color]} bariers
                    :when (< bird-position position (+ position 800))]
                (barier {:x (- position bird-position)
                         :height height
                         :direction direction
                         :color color}))))

(def state (atom {:bird-position 0
                  :bird-y 240
                  :bariers [{:position 50 :height 200 :color :red :direction :up}
                            {:position 150 :height 50 :color :green :direction :down}]}))

(r/init! view state
         {:canvas (.getElementById js/document "canvas")})

(go-loop []
  (swap! state update-in [:bird-position] + 5)
  (<! (timeout 50))
  (recur))
