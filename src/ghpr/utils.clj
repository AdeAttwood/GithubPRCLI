(ns ghpr.utils
  (:require [babashka.process :as ps]))

(defn get-editor []
  (or (System/getenv "EDITOR") "nvim"))

(defn edit-file [file]
  (ps/check (ps/process {:in :inherit :out :inherit} (get-editor) file )))

