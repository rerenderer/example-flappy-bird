(defproject rerenderer-example-flappy-bird "0.1.0-SNAPSHOT"
  :description "Flappy bird written with rerenderer"
  :url "https://github.com/rerenderer/example-flappy-bird"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [org.clojure/core.async "0.2.371"]
                 [org.rerenderer/rerenderer "0.4.0"]]
  :plugins [[lein-cljsbuild "1.1.2"]
            [lein-figwheel "0.5.0-6"]]
  :source-paths ["src" "script"]
  :cljsbuild {:builds {:main {:source-paths ["src"]
                              :figwheel {:websocket-host "localhost"
                                         :repl false}
                              :compiler {:output-to "resources/public/compiled/main.js"
                                         :output-dir "resources/public/compiled"
                                         :asset-path "/compiled"
                                         :source-map true
                                         :main "rerenderer-example-flappy-bird.core"
                                         :optimizations :none
                                         :pretty-print false}}}}
  :figwheel {:nrepl-port 7888
             :repl false}
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]})
