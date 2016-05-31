(def command-history (atom []))
(def undo-history (atom ()))
(def state (atom []))
(def pen (atom true))

(defmulti execute :command)

(defmethod execute "Move"
  [{:keys [value]}]
  (swap! state conj (assoc {} :x (read-string value) :angle 0 :command (str "Move" " " value))))

(defmethod execute "Turn"
  [{:keys [value]}]
  (swap! state conj (assoc {} :x 0 :angle (read-string value))))

(defmethod execute "Pen" [{:keys [value]}]
  (if (= value "up")
  (reset! pen false)
    (if (= value "down")
    (reset! pen true))))

(defn get-command  [line]
  (if (empty? line) ()
    (let [line (clojure.string/split line #"\s+")
          command (first line)
          value (second line)]
      (execute (assoc {} :value value :command command )))))

(defn read-commands [filepath]
 (with-open [rdr (clojure.java.io/reader filepath)]
    ;(reset! command-history (reduce get-command [] (line-seq rdr))))
  (doseq [x (line-seq rdr)]
  (get-command x))))

(read-commands "/Users/adarshkumar/Desktop/clojure-assignments/assignment3/resources/sample.txt")



(doseq [ x @command-history]
  (execute x))
;(peek @state)

;(reduce execute @command-history)
((first @state) :x)
(count @state)
@pen
@state

(->> :x (- 4 1) (nth @state))

(clojure.string/join " " (vals (first @command-history)))

(str "Move")

;(defmethod execute "Pen up" []
 ; (swap! state assoc :pen true))

;(defmethod execute "Pen down" []
 ; (swap! state assoc :pen false))

;(reset! command-history (rest @command-history))
;(swap! undo-history conj (first @command-history))
;(deref undo-history)

(defn keyup-pressed []
 (execute (first @command-history))
 (swap! undo-history conj (first @command-history))
  (reset! command-history (rest @command-history)))

;(defn keyboard-action []
 ; (let [key (q/key-as-keyword)]
    ;(if (= key UP) (keyup-pressed))))
