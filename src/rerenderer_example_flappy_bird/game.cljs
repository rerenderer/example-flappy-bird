(ns rerenderer-example-flappy-bird.game
  (:require-macros [cljs.core.async.macros :refer [go-loop]])
  (:require [cljs.core.match :refer-macros [match]]
            [cljs.core.async :refer [<! >! timeout]]))

(def tick 50)

(defn start-timer!
  [event-ch]
  (go-loop []
    (<! (timeout tick))
    (>! event-ch [:tick])
    (recur)))

(defn is-barier-here?
  [bariers position y]
  (some true?
        (for [barier bariers
              :let [barier-position (:position barier)
                    {:keys [direction height]} barier]]
          (cond
            (not (<= barier-position position (+ barier-position 52))) false
            (and (= direction :up) (> y (- 512 height))) true
            (and (= direction :down) (< y height)) true
            :else false))))

(defn is-alive?
  "Returns `true` when bird is alive."
  [state]
  (let [{:keys [bird-position bird-y bariers]} @state]
    (cond
      (neg? bird-y) false
      (> bird-y 512) false
      (is-barier-here? bariers bird-position bird-y) false
      :else true)))


(defn generate-bariers!
  [from to state]
  (let [cleaned-bariers (remove #(< (:position %) (- from 100))
                                (:bariers @state))
        new-bariers (loop [current (+ from 10)
                           result []]
                      (if (< current to)
                        (let [direction (rand-nth [:up :down])
                              color (rand-nth [:red :green])
                              height (+ (rand-int 200) 50)
                              next (+ (rand-int 50) 100 current)]
                          (recur next (conj result {:direction direction
                                                    :color color
                                                    :height height
                                                    :position current})))
                        result))]
    (swap! state assoc :bariers
           (concat cleaned-bariers new-bariers))))

(defn move-bird!
  [state]
  (when (:running? @state)
    (swap! state update-in [:bird-position] + 5)
    (let [bird-position (:bird-position @state)]
      (when (zero? (mod bird-position 800)) ; TODO: consts
        (generate-bariers! bird-position
                           (+ 800 bird-position)
                           state)))
    (when-not (is-alive? state)
      (swap! state assoc :running? false))))

(defn start!
  [state initial-state]
  (reset! state initial-state)
  (swap! state assoc :running? true)
  (generate-bariers! 50 800 state))

(defn subscribe!
  [event-ch state initial-state]
  (start-timer! event-ch)
  (start! state initial-state)
  (go-loop []
    (match (<! event-ch)
      [:start] (start! state initial-state)
      [:tick] (move-bird! state))
    (recur)))
