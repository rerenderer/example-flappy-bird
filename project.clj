(defproject rerenderer-example-flappy-bird "0.1.0-SNAPSHOT"
  :description "Flappy bird written with rerenderer"
  :url "https://github.com/rerenderer/example-flappy-bird"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.48"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [rerenderer "0.1.0-SNAPSHOT"]
                 [figwheel-sidecar "0.4.1"]]
  :plugins [[lein-cljsbuild "1.1.0"]
            [lein-figwheel "0.4.1"]]
  :source-paths ["src" "script"]
  :cljsbuild {:builds {:main {:source-paths ["src" "../rerenderer/src/"]
                              :figwheel {:websocket-host "nvbn-XPS13-9333.local"
                                         :repl false}
                              :compiler {:output-to "resources/public/compiled/main.js"
                                         :output-dir "resources/public/compiled"
                                         :asset-path "/compiled"
                                         :source-map true
                                         :main "rerenderer-example-flappy-bird.core"
                                         :optimizations :none
                                         :pretty-print false}}}}
  :figwheel {:nrepl-port 6700})
