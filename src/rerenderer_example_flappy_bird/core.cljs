(ns rerenderer-example-flappy-bird.core
  (:require-macros [cljs.core.async.macros :refer [go-loop]])
  (:require [cljs.core.async :refer [<! timeout chan]]
            [rerenderer-example-flappy-bird.views :refer [root]]
            [rerenderer-example-flappy-bird.game :refer [subscribe!]]
            [rerenderer.core :as r]))

(enable-console-print!)

(def initial-state
  {:running? false
   :bird-position 0
   :bird-y 240
   :bariers [{:position 50 :height 200 :color :red :direction :up}
             {:position 150 :height 50 :color :green :direction :down}]})


(let [state (atom initial-state)
      event-ch (chan)
      options {:canvas (.getElementById js/document "canvas")}]
  (r/init! root state options)
  (r/listen! event-ch :click options)
  (subscribe! event-ch state initial-state))
