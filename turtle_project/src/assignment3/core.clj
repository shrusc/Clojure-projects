(ns assignment3.core
  (:require [quil.core :as q])
  (:require [clojure.string :as string])
  (:require [clojure.java.io :as io]))

(def turtle-moves (atom []))
(def move-index (atom 0))
(def stroke-value (atom true))
;file-read is set to 1 if the turtle program is already read from the file
(def file-read (atom 0))

;Multimethod to parse each command and populate the turtle-moves
(defmulti excecute-move :command)

(defmethod excecute-move "move"
  [{:keys [value]}]
  (swap! turtle-moves conj
    (assoc {} :x (read-string value)
              :angle 0
              :stroke @stroke-value
              :command (str "Move" " " value))))

(defmethod excecute-move "turn"
  [{:keys [value]}]
  (swap! turtle-moves conj
    (assoc {} :x 0
              :angle (read-string value)
              :stroke @stroke-value
              :command (str "Turn" " " value))))

(defmethod excecute-move "pen"
  [{:keys [value]}]
  (if (= value "up")
    (reset! stroke-value false)
    (reset! stroke-value true))
  (swap! turtle-moves conj
    (assoc {} :x 0
              :angle 0
              :stroke false
              :command (str "Pen" " " value))))

;Parses each line from the program
(defn get-turtle-command [line]
  (let [split-line (string/split line #"\s+")
        command (first split-line)
        value (second split-line)]
    (excecute-move (assoc {} :value value :command command))))

;Reads the program from the file
(defn read-turtle-commands [filepath]
 ;setting file-read to 1 to make sure the commands are not read again
  (reset! file-read 1)
  (with-open [rdr (io/reader filepath)]
    (doseq [x (line-seq rdr)]
      (get-turtle-command x))))

;Handles the right key press
(defn keyright-pressed []
  (when (< @move-index (count @turtle-moves))
    (swap! move-index inc))
  (q/redraw))

;Handles the left key press
(defn keyleft-pressed []
  (when (> @move-index 0)
    (swap! move-index dec))
  (q/redraw))

;Handles the r key press
(defn keyr-pressed []
  (reset! move-index (count @turtle-moves))
  (q/start-loop))

;Handles all keyboard interaction
(defn keyboard-action []
  (let [key (q/key-as-keyword)]
    (case key :left (keyleft-pressed)
              :right (keyright-pressed)
              :r (keyr-pressed))))

;setup function called only once, during sketch initialization.
(defn setup []
  (when (= @file-read 0)
    (read-turtle-commands (-> "sample.txt" io/resource io/file)))
  (q/background 240)
  (q/frame-rate 20)
  (q/no-loop))

;Handles all the drawing done in the Quil window.
(defn draw []
  (q/background 240)
  (q/push-matrix)
  (q/translate (/ (q/width) 2) (/ (q/height) 2))
  (doseq [x (take @move-index @turtle-moves)]
    (if (x :stroke)
      (q/stroke 0)
      (q/no-stroke))
    (q/line 0 0 (x :x) 0)
    (q/translate (x :x) 0)
    (q/rotate (q/radians (x :angle))))
  (q/pop-matrix)
  (q/fill 0)
  (q/text ((nth @turtle-moves (- @move-index 1)) :command) 20 20))

;Start of the program where the commands are read from the file and a sketch is drawn
(defn start-program []
  (q/defsketch assignment3
       :title "Assignment 3"
       :size [500 500]
       :setup setup
       :draw draw
       :key-pressed keyboard-action
       :features [:keep-on-top]))

(start-program)



