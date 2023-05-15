(ns ghpr.utils
  (:require [babashka.process :as ps]))

(defn edit-file [file]
  (ps/check (ps/process {:in :inherit :out :inherit} "nvim" file )))

