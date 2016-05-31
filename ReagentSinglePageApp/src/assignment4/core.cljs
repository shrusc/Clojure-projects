(ns assignment4.core
  (:require [reagent.core :as reagent :refer [atom]]
            ;[goog.events :as events]
            [goog.history.EventType :as EventType]
            [goog.events :as e]
            [schema.core :as s
             :include-macros true ;; cljs only
             ]))

(enable-console-print!)

(s/defschema point-schema
  "A schema for the mouse click position on screen"
  {:x s/Num :y s/Num})

(s/defschema current-mouse-state-schema
  "A schema for the current mouse state"
  {:start point-schema :end point-schema :second-click s/Bool})

(s/defschema event-list-schema
  "A schema for the events draw and mode change"
  (s/conditional
    #(= (:event %) :draw) {:event (s/eq :draw) :shape s/Str :start point-schema :end point-schema}
    #(= (:event %) :mode-change) {:event (s/eq :mode-change) :last-mode s/Str}))

(s/defschema app-state-schema
  "A schema for the app-state Big Ratom"
  {:text s/Str
   :current-mode s/Str
   :event-list [event-list-schema]
   :current-mouse-state current-mouse-state-schema})

(defonce app-state
  (reagent/atom (s/validate app-state-schema
                            {:text "Drawing tool"
                             :current-mode "line"
                             :event-list []
                             :current-mouse-state {:start {:x 0 :y 0}
                                                   :end {:x 0 :y 0}
                                                   :second-click false}})))


(def mouse-state (reagent/cursor app-state [:current-mouse-state]))

(def events (reagent/cursor app-state [:event-list]))

(def mode (reagent/cursor app-state [:current-mode]))

(defn menu-button-click [clicked-mode]
  "Function to handle the menu button click"
  ;not adding the mode to the event list if its the current mode
  (when (not (= clicked-mode @mode))
    (swap! events conj (s/validate
                          event-list-schema
                          (assoc {} :event :mode-change :last-mode @mode)))
    (swap! app-state assoc :current-mode clicked-mode)))

(defn undo-click []
  "Function to handle undo button click"
  (if (empty? @events) []
    (let [all-events @events
          last-event (peek @events)]
      (case (last-event :event)
        :draw (reset! events (pop all-events))
        :mode-change ((swap! app-state assoc :current-mode (last-event :last-mode))
                      (reset! events (pop all-events)))))))

(defn clear-click []
  "Function to handle clear button click"
  (reset! events [])
  (swap! mouse-state assoc :start {:x 0 :y 0} :end {:x 0 :y 0} :second-click false))

(defn lister [items]
  "Function to display the pallete buttons"
  [:center
    [:ul {:style {:text-align "center"}}
      [:li {:style {:list-style-type "none" :padding "5px" :width "20px"}}
        [:button {:on-click #(menu-button-click "line")}
         "Line"]]
      [:li {:style {:list-style-type "none" :padding "5px" :width "20px"}}
        [:button {:on-click #(menu-button-click "rect")}
         "Rect"]]
     [:li {:style {:list-style-type "none" :padding "5px" :width "20px"}}
      [:button {:on-click #(menu-button-click "circle")}
       "Circle"]]
     [:li {:style {:list-style-type "none" :padding "5px" :width "20px"}}
      [:button {:on-click #(undo-click)}
       "Undo"]]
     [:li {:style {:list-style-type "none" :padding "5px" :width "20px"}}
      [:button {:on-click #(clear-click)}
       "Clear"]]]])


(defn draw-line [x1 y1 x2 y2]
  "Function to draw a line"
  [:line {:x1 x1
          :y1 y1
          :x2 x2
          :y2 y2
          :stroke "red"}])

(defn draw-circle [x1 y1 x2 y2]
  "Function to draw a circle"
  (let [x-square (.pow js/Math (- x2 x1) 2)
        y-sqaure (.pow js/Math (- y2 y1) 2)]
    [:circle {:cx x1
              :cy y1
              :r (.sqrt js/Math (+ x-square y-sqaure))
              :stroke "red"
              :fill "red"}]))

(defn x-min [x1 x2]
  (if (< x1 x2) x1 x2))

(defn y-min [y1 y2]
  (if (< y1 y2) y1 y2))

(defn draw-rect [x1 y1 x2 y2]
  "Function to draw a rectangle"
  [:rect {:x (x-min x1 x2)
          :y (y-min y1 y2)
          :width (.abs js/Math (- x2 x1))
          :height (.abs js/Math (- y2 y1))
          :stroke "red"
          :fill "red"}])

(defn first-mouse-click [e]
  "Handles the 1st mouse click"
  (swap! mouse-state assoc :second-click true
                           :start {:x (- (.-clientX e) 400) :y (- (.-clientY e) 100)}
                           :end {:x (- (.-clientX e) 400) :y (- (.-clientY e) 100)}))

(defn second-mouse-click [e]
  "Handles the 2nd mouse click"
  (swap! mouse-state assoc :end {:x (- (.-clientX e) 400) :y (- (.-clientY e) 100)})
  (swap! events conj (s/validate event-list-schema
                                 (assoc {} :event :draw
                                           :shape @mode
                                           :start {:x (-> @mouse-state :start :x) :y (-> @mouse-state :start :y)}
                                           :end {:x (-> @mouse-state :end :x) :y (-> @mouse-state :end :y)})))
  (swap! mouse-state assoc :start {:x 0 :y 0} :end {:x 0 :y 0} :second-click false))

(defn draw-current-shape []
  "Function to display the current drawing"
  (case @mode
    "line" (draw-line
             (-> @mouse-state :start :x)
             (-> @mouse-state :start :y)
             (-> @mouse-state :end :x)
             (-> @mouse-state :end :y))
    "rect" (draw-rect
             (-> @mouse-state :start :x)
             (-> @mouse-state :start :y)
             (-> @mouse-state :end :x)
             (->@mouse-state :end :y))
    "circle" (draw-circle
               (-> @mouse-state :start :x)
               (-> @mouse-state :start :y)
               (-> @mouse-state :end :x)
               (-> @mouse-state :end :y))))

(defn draw-previous-shapes []
  "Function to display the previous drawn shapes"
  (for [x @events]
    (when (= (x :event) :draw)
      (case (x :shape)
        "line" (draw-line (-> x :start :x) (-> x :start :y) (-> x :end :x) (-> x :end :y))
        "rect" (draw-rect (-> x :start :x) (-> x :start :y) (-> x :end :x) (-> x :end :y))
        "circle" (draw-circle (-> x :start :x) (-> x :start :y) (-> x :end :x) (-> x :end :y))))))

(defn drawing-tool []
  "Reagent render function"
  [:center
    [:h1 (:text @app-state)]
    [:div
      {:width 100
       :height 50
       :style {:position :fixed :top 100 :left 100 :border "black solid 3px" :padding "10px"}}
      "Select a mode" [:br] [:br]
      "Current mode: " (str @mode)
      [lister (range 4)]]
    [:svg
      {:width 700
       :height 700
       :style {:position :fixed :top 100 :left 400 :border "black solid 3px"}
       :on-click (fn mouse-click [e]
                   (case (@mouse-state :second-click)
                     false (first-mouse-click e)
                     true (second-mouse-click e)))
       :on-mouse-move (fn mouse-move [e]
                        (when (@mouse-state :second-click)
                          (swap! mouse-state assoc :end {:x (- (.-clientX e) 400)
                                                         :y (- (.-clientY e) 100)})))}
      (list
        (when (@mouse-state :second-click)
          (draw-current-shape))
        (when (> (count @events) 0)
          (draw-previous-shapes)))]])


(reagent/render-component [drawing-tool]
                          (. js/document (getElementById "app")))

(defn on-js-reload [])

