import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;

import view.IView;
import view.View;

import javax.swing.SwingUtilities;

import model.IModel;
import model.Model;
import presenter.IPresenter;
import presenter.Presenter;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            setLookAndFeel();
            
            IModel model = new Model();
            IView view = new View();
            IPresenter presenter = new Presenter(model, view);
            presenter.start();
        });
    }
    
    private static void setLookAndFeel(){
        try{
            FlatDarkFlatIJTheme.setup();
            
        }catch (Exception e){}
    }
}
/*
// ----------------------- TEMAS ---------------------------------------
Base
// import javax.swing.UIManager;
// UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
// UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
// UIManager.getSystemLookAndFeelClassName();

Libreria FlatLaf
// import com.formdev.flatlaf.intellijthemes.*
// ----------------------
// Oscuros
// ----------------------
// like
FlatDarkFlatIJTheme
FlatNordIJTheme
FlatMTMoonlightIJTheme
FlatHiberbeeDarkIJTheme
FlatDraculaIJTheme
FlatArcDarkOrangeIJTheme

// meh
FlatXcodeDarkIJTheme
FlatMTMonokaiProIJTheme
FlatMTMaterialDarkerIJTheme
FlatGradiantoDeepOceanIJTheme
FlatGradiantoMidnightBlueIJTheme
FlatArcDarkIJTheme
FlatCarbonIJTheme
FlatCobalt2IJTheme
FlatDarkPurpleIJTheme
FlatGradiantoDarkFuchsiaIJTheme
FlatGradiantoNatureGreenIJTheme
FlatGruvboxDarkHardIJTheme
FlatMonokaiProIJTheme
FlatHighContrastIJTheme
FlatMaterialDesignDarkIJTheme
FlatMonocaiIJTheme
FlatOneDarkIJTheme
FlatSolarizedDarkIJTheme
FlatSpacegrayIJTheme
FlatVuesionIJTheme
FlatMTArcDarkIJTheme
FlatMTAtomOneDarkIJTheme
FlatMTDraculaIJTheme
FlatMTGitHubDarkIJTheme
FlatMTMaterialDeepOceanIJTheme
FlatMTMaterialOceanicIJTheme
FlatMTMaterialPalenightIJTheme
FlatMTNightOwlIJTheme,
FlatMTSolarizedDarkIJTheme
// ----------------------
// Claros
// ----------------------
FlatArcIJTheme, 
FlatArcOrangeIJTheme
FlatCyanLightIJTheme
FlatGrayIJTheme
FlatLightFlatIJTheme
FlatSolarizedLightIJTheme
FlatMTAtomOneLightIJTheme, 
FlatMTGitHubIJTheme, 
FlatMTLightOwlIJTheme, 
FlatMTMaterialLighterIJTheme, 
FlatMTSolarizedLightIJTheme 
*/