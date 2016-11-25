+ Λίγα λόγια για την εφαρμογή. (Για μορφή PDF, παρακαλώ κατεβάστε το "./README.pdf"

 + Για την εφαρμογή μας χρησιμοποιήσαμε δύο αισθητήρες των κινητών. Αυτοί είναι ο
light_sensor και ο proximity_sensor. Καθώς τρέχει η εφαρμογή γίνεται παράλληλος έλεγχος
και από τους δύο αισθητήρες.
Για τον proximity_sensor είναι προφανές ότι μόλις η συσκευή πλησιάσει σε μια επιφάνεια θα
εμφανιστεί το αντίστοιχο προειδοποιητικό μήνυμα.
 + Για τον light_sensor έστω η παραδοχή ότι αν η συσκευή πλησιάσει σε κάποιο εμπόδιο, αυτό
θα δημιουργήσει σκιά προς την συσκευή και έτσι θα μειώσει την φωτεινότητα στον
αισθητήρα. Κατά την εκκίνηση του προγράμματος παίρνουμε μέσο όρο από τις τιμές του
light_sensor. Για να μπορέσουμε να κινηθούμε σε ένα φυσικό χώρο(όπου κάποια σημεία είναι
πιο φωτεινά ή σκοτεινά), όταν αυξηθεί το φώς ξαναπαίρνουμε μέσο όρο των τιμών. Όταν
μειωθεί το φως εμφανίζεται το προειδοποιητικό μήνυμα και μετά από δέκα δευτερόλεπτα
σταματάει, με την παραδοχή ότι η συσκευή δεν κινείται σε κάποιο εμπόδιο αλλά έχει βρεθεί
σε ένα ποιο σκοτεινό χώρο.
+ Ρυθμίσεις της εφαρμογής.
 + Στις ρυθμίσεις της εφαρμογής υπάρχει η επιλογή για το κατώφλι του light_sensor. Είναι ένα
drop_down menu με τις επιλογές (50 ,60, 75) όπου αναφέρονται στο ποσοστό προστασίας. Για
παράδειγμα, για την επιλογή (60) θα έχουμε 60% προστασία, δηλαδή όταν το φως μειωθεί
παραπάνω από το 40% του μέσου όρου τότε η εφαρμογή θα μας προειδοποιήσει για πιθανή
σύγκρουση.
 + Για το proximity_sensor υπάρχει ένα seek bar με τις τιμές του να ξεκινάνε από το μηδέν έως το
maximum range της εκάστοτε συσκευής.
Τέλος υπάρχει άλλο ένα drop_down menu για την συχνότητα λήψης τιμών από τους
αισθητήρες. Έχει τις επιλογές (SLOW, FAST). (Σε δοκιμές που κάναμε σε κινητά με μικρή οθόνη
η επιλογή για την συχνότητα φαίνεται μπερδεμένη με το seek bar από το proximity, όμως
δουλεύει κανονικά.)
+ Δοκιμή
Για να τρέξετε την εφαρμογή θα βρείτε στα αρχεία μας ένα APK. Εγκαταστήστε το σε μια
συσκευή android και τρέξτε το.(Πρώτα ενεργοποιήστε τις άγνωστες πηγές στο κινητό σας)