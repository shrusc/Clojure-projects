;(:require [clojure.string :as string])


(def command-history (atom []))
(def state (atom []))
(def index (atom 0))
(def stroke-color (atom 0))
(def message (atom "Please press fwd or r keys key to start"))

(defmulti execute :command)

(defmethod execute "Move"
  [{:keys [value]}]
  (swap! state conj (vector (read-string value) 0 @stroke-color)))

(defmethod execute "Turn"
  [{:keys [value]}]
  (swap! state conj (vector 0 (read-string value) @stroke-color)))

(defmethod execute "Pen" [{:keys [value]}]
  (if (= value "up")
  (reset! stroke-color 240)
    (reset! stroke-color 0))
  (swap! state conj (vector 0 0 @stroke-color)))

(defn get-command [result line]
  (if (empty? line) result
    (let [line (clojure.string/split line #"\s+")
          command (first line)
          value (second line)]
      (conj result (assoc {} :value value :command command)))))

(defn read-commands [filepath]
 (with-open [rdr (clojure.java.io/reader filepath)]
    (reset! command-history (reduce get-command [] (line-seq rdr))))
  (doseq [x @command-history]
  (execute x)))

(read-commands "/Users/adarshkumar/Desktop/clojure-assignments/assignment3/sample.txt")

@state

@command-history
