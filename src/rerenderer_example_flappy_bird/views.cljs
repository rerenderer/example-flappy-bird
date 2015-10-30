(ns rerenderer-example-flappy-bird.views
  (:require [cljs.core.async :refer [<! timeout]]
            [cljs.core.match :refer-macros [match]]
            [rerenderer.primitives :as p]))

(enable-console-print!)

(def screen-height 512)
(def screen-width 800)
(def sprite-atlas "bird")


(defn bird
  "Renders bird."
  [{:keys [x y]}]
  (p/image {:width 36
            :height 36
            :sx 4
            :sy 980
            :x x
            :y (- screen-height y)
            :src sprite-atlas}))


(defn background
  "Renders background with passed offset."
  [{:keys [offset]} & childs]
  (p/rectangle {:width screen-width
                :height screen-height}
               (p/rectangle {:width 1088
                             :height screen-height
                             :x (/ (- 0 offset) 2)}
                            (for [x (range 0 1088 288)]
                              (p/image {:width 288
                                        :height screen-height
                                        :sx 0
                                        :sy 0
                                        :x x
                                        :y 0
                                        :src sprite-atlas})))
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
    (- screen-height height)
    0))

(defn barier
  "Renders single barier/tube."
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
              :src sprite-atlas})))

(defn start-game-badge
  []
  (p/rectangle {:width 500
                :height 80
                :color [255 0 255 0]
                :x 100
                :y 100}
               (p/text {:width 500
                        :height 80
                        :color [255 255 0 0]
                        :font-size 50}
                       "Click to start!")))

(defn root
  "Renders full scene with game and menu."
  [{:keys [bird-position bariers bird-y running?]}]
  (background {:offset (mod bird-position 572)}
              (if running?
                [(bird {:x 50 :y bird-y})
                 (for [{:keys [position height direction color]} bariers
                       :when (< bird-position position (+ position screen-width))]
                   (barier {:x (- position bird-position)
                            :height height
                            :direction direction
                            :color color}))]
                (start-game-badge))))
