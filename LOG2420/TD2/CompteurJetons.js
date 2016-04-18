function CompteurJetons() {

  var compte = 0;
  var totalWords = 0;
  var pourcent = 0;

  /**
   * Cette fonction retourne de jetons comptés
   */
  this.getJetons = function () {
    return compte * totalWords;
  }

  /**
   * Cette fonction retourne le progès du compte, avec une valeur entre 0 et 100
   */
  this.getProgress = function () {
    return pourcent;
  }

  /**
   * Cette fonction compte les jetons et retourne un objet de type Generator
   * Voir documentation: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/function*
   */
  this.compterJetons = function * (str) {
    totalWords = str.split(' ').length;
    while(compte < totalWords) {
      for(i=0;i<10000000;i++){}
        ++compte;
      pourcent = Math.round(compte*100/totalWords);
      yield this.getProgress();
    }
  }
}