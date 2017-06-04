package br.com.yahoo.mau_mss.deletetomcatfilesplugin;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.awt.StatusDisplayer;
import org.openide.util.NbBundle.Messages;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Title: DeleteTempAction
 * Description: This plugin will try to perform the following tasks:
 * - Delete all but ROOT.xml or manager.xml at:
 * C:\Users\<user-dir>\AppData\Roaming\NetBeans\X.X.X\apache-tomcat-X.X.X.X_base\conf\Catalina\localhost
 * - Delete all at
 * C:\Users\<user-dir>\AppData\Roaming\NetBeans\X.X.X\apache-tomcat-X.X.X.X_base\logs
 * - Delete all at
 * C:\Users\<user-dir>\AppData\Roaming\NetBeans\X.X.X\apache-tomcat-X.X.X.X_base\temp
 * - Delete all at
 * C:\Users\<user-dir>\AppData\Roaming\NetBeans\X.X.X\apache-tomcat-X.X.X.X_base\webapps
 * - Delete all but /ROOT or /manager at:
 * C:\Users\<user-dir>\AppData\Roaming\NetBeans\X.X.X\apache-tomcat-X.X.X.X_base\work\Catalina\localhost
 * Date: September 23, 2014, 9:20:00 PM
 *
 * @author Mauricio Soares da Silva (mauricio.soares)
 * @see https://platform.netbeans.org/tutorials/nbm-copyfqn.html
 */
@ActionID(
        category = "File",
        id = "br.com.yahoo.mau_mss.deletetomcatfilesplugin.DeleteTempAction"
)
@ActionRegistration(
        iconBase = "resources/trashEmpty.png",
        displayName = "#CTL_DeleteTempAction"
)
@ActionReference(path = "Menu/Tools", position = 190)
@Messages("CTL_DeleteTempAction=Clear Tomcat")
public class DeleteTempAction implements ActionListener {
  private String baseDir;
  private static final String TOMCAT_PREFIX = "apache-tomcat";
  private static final String ROOT_NAME = "ROOT";
  private static final String MANAGER_NAME = "manager";
  private static final Logger logger = Logger.getLogger(DeleteTempAction.class.getName());

  @Override
  public void actionPerformed(ActionEvent e) {
    mountTomcatBase();
    NotifyDescriptor d = new NotifyDescriptor.Confirmation(
        String.format("Do you really want to delete Tomcat logs and temp files at %s?", this.baseDir),
        "Confirmation", NotifyDescriptor.OK_CANCEL_OPTION);
    if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.CANCEL_OPTION) {
      StatusDisplayer.getDefault().setStatusText("Operation cancelled");
      return;
    }
    if (apagarConf() & apagarLogs() & apagarTemp() & apagarWebApps() & apagarWork()) {
      StatusDisplayer.getDefault().setStatusText("Great, all Tomcat logs and temp files were deleted with success!");
    } else {
      StatusDisplayer.getDefault().setStatusText("Sorry, some files could not been deleted.");
    }
  }

  protected void mountTomcatBase() {
    StringBuilder sb = new StringBuilder();
    sb.append(SystemUtils.USER_HOME);
    sb.append(SystemUtils.FILE_SEPARATOR);
    sb.append("AppData");
    sb.append(SystemUtils.FILE_SEPARATOR);
    sb.append("Roaming");
    sb.append(SystemUtils.FILE_SEPARATOR);
    sb.append("NetBeans");
    sb.append(SystemUtils.FILE_SEPARATOR);
    sb.append(getVersaoNetBeans(sb.toString()));
    sb.append(SystemUtils.FILE_SEPARATOR);
    sb.append(getDirApache(sb.toString()));
    this.baseDir = sb.toString();
  }

  private String getVersaoNetBeans(String path) {
    File pathFile = FileUtils.getFile(path);
    File[] folders = pathFile.listFiles((FileFilter) FileFilterUtils.directoryFileFilter());
    if (folders == null) {
      DeleteTempAction.logger.log(Level.WARNING, "Nenhum diret\u00f3rio do NetBeans encontrado em {0}", path);
      return "";
    }
    String maiorVersao = "";
    for (File file : folders) {
      if (!Character.isDigit(file.getName().charAt(0))) {
        continue;
      }
      if (file.getName().compareTo(maiorVersao) > 0) {
        maiorVersao = file.getName();
      }
    }
    return maiorVersao;
  }

  private String getDirApache(String path) {
    File pathFile = FileUtils.getFile(path);
    File[] folders = pathFile.listFiles((FileFilter) FileFilterUtils.directoryFileFilter());
    if (folders == null) {
      DeleteTempAction.logger.log(Level.WARNING, "Nenhum diret\u00f3rio do apache encontrado em {0}", path);
      return "";
    }
    String pathApache = "";
    for (File file : folders) {
      if (StringUtils.startsWithIgnoreCase(file.getName(), DeleteTempAction.TOMCAT_PREFIX)) {
        pathApache = file.getName();
      }
    }
    return pathApache;
  }

  private boolean apagarConf() {
    boolean noErrorsFounde = true;
    for (File file : findXMLFiles(getConfDir())) {
      if (!StringUtils.startsWithIgnoreCase(file.getName(), DeleteTempAction.MANAGER_NAME)
              && !StringUtils.startsWithIgnoreCase(file.getName(), DeleteTempAction.ROOT_NAME)) {
        DeleteTempAction.logger.log(Level.INFO, "Apagando o arquivo {0}", file.getAbsolutePath());
        if (!FileUtils.deleteQuietly(file)) {
          noErrorsFounde = false;
        }
      }
    }
    return noErrorsFounde;
  }

  private Collection<File> findXMLFiles(String path) {
    final String[] SUFFIX = {"xml"};
    File pathFile = FileUtils.getFile(path);
    return FileUtils.listFiles(pathFile, SUFFIX, false);
  }

  private String getConfDir() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.baseDir);
    sb.append(SystemUtils.FILE_SEPARATOR);
    sb.append("conf");
    sb.append(SystemUtils.FILE_SEPARATOR);
    sb.append("Catalina");
    sb.append(SystemUtils.FILE_SEPARATOR);
    sb.append("localhost");
    return sb.toString();
  }

  private boolean apagarLogs() {
    boolean noErrorsFounde = true;
    for (File file : findLogFiles(getLogDir())) {
      DeleteTempAction.logger.log(Level.INFO, "Apagando o arquivo {0}", file.getAbsolutePath());
      if (!FileUtils.deleteQuietly(file)) {
        noErrorsFounde = false;
      }
    }
    return noErrorsFounde;
  }

  private Collection<File> findLogFiles(String path) {
    final String[] SUFFIX = {"log", "txt"};
    File pathFile = FileUtils.getFile(path);
    return FileUtils.listFiles(pathFile, SUFFIX, false);
  }

  private String getLogDir() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.baseDir);
    sb.append(SystemUtils.FILE_SEPARATOR);
    sb.append("logs");
    return sb.toString();
  }

  private boolean apagarTemp() {
    boolean noErrorsFounde = true;
    for (File file : findAllFiles(getTempDir())) {
      DeleteTempAction.logger.log(Level.INFO, "Apagando o arquivo {0}", file.getAbsolutePath());
      if (!FileUtils.deleteQuietly(file)) {
        noErrorsFounde = false;
      }
    }
    return noErrorsFounde;
  }

  private String getTempDir() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.baseDir);
    sb.append(SystemUtils.FILE_SEPARATOR);
    sb.append("temp");
    return sb.toString();
  }

  private boolean apagarWebApps() {
    boolean noErrorsFounde = true;
    for (File file : findAllFiles(getWebAppsDir())) {
      DeleteTempAction.logger.log(Level.INFO, "Apagando o arquivo {0}", file.getAbsolutePath());
      if (!FileUtils.deleteQuietly(file)) {
        noErrorsFounde = false;
      }
    }
    return noErrorsFounde;
  }

  private String getWebAppsDir() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.baseDir);
    sb.append(SystemUtils.FILE_SEPARATOR);
    sb.append("webapps");
    return sb.toString();
  }

  private Collection<File> findAllFiles(String path) {
    File pathFile = FileUtils.getFile(path);
    return FileUtils.listFiles(pathFile, null, false);
  }

  private boolean apagarWork() {
    File pathFile = FileUtils.getFile(getWorkDir());
    File[] folders = pathFile.listFiles((FileFilter) FileFilterUtils.directoryFileFilter());
    boolean noErrorsFounde = true;
    if (folders == null) {
      DeleteTempAction.logger.log(Level.INFO, "Nenhum diret\u00f3rio em {0} a apagar", pathFile.getAbsolutePath());
      return noErrorsFounde;
    }
    for (File file : folders) {
      if (!StringUtils.startsWithIgnoreCase(file.getName(), DeleteTempAction.MANAGER_NAME)
              && !StringUtils.startsWithIgnoreCase(file.getName(), DeleteTempAction.ROOT_NAME)) {
        DeleteTempAction.logger.log(Level.INFO, "Apagando diret\u00f3rio {0}", file.getAbsolutePath());
        if (!FileUtils.deleteQuietly(file)) {
          noErrorsFounde = false;
        }
      }
    }
    return noErrorsFounde;
  }

  private String getWorkDir() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.baseDir);
    sb.append(SystemUtils.FILE_SEPARATOR);
    sb.append("work");
    sb.append(SystemUtils.FILE_SEPARATOR);
    sb.append("Catalina");
    sb.append(SystemUtils.FILE_SEPARATOR);
    sb.append("localhost");
    return sb.toString();
  }

  protected String getBaseDir() {
    return this.baseDir;
  }

}
